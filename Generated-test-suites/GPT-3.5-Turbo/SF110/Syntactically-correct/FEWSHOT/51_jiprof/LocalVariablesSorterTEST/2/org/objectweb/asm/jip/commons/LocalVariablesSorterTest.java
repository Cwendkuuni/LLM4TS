package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.*;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.commons.Type;

public class LocalVariablesSorterTest {

    private MethodVisitor mv;
    private LocalVariablesSorter lvs;

    @Before
    public void setUp() {
        mv = mock(MethodVisitor.class);
        lvs = new LocalVariablesSorter(0, "desc", mv);
    }

    @Test
    public void testVisitVarInsn() {
        lvs.visitVarInsn(Opcodes.ILOAD, 1);
        verify(mv).visitVarInsn(Opcodes.ILOAD, 1);
    }

    @Test
    public void testVisitIincInsn() {
        lvs.visitIincInsn(1, 1);
        verify(mv).visitIincInsn(1, 1);
    }

    @Test
    public void testVisitMaxs() {
        lvs.visitMaxs(10, 20);
        verify(mv).visitMaxs(10, lvs.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        lvs.visitLocalVariable("name", "desc", "signature", new Label(), new Label(), 1);
        verify(mv).visitLocalVariable("name", "desc", "signature", any(Label.class), any(Label.class), eq(1));
    }

    @Test
    public void testVisitFrame_NotExpandedFrames() {
        try {
            lvs.visitFrame(Opcodes.F_NEW, 0, new Object[0], 0, new Object[0]);
        } catch (IllegalStateException e) {
            assertEquals("ClassReader.accept() should be called with EXPAND_FRAMES flag", e.getMessage());
        }
    }

    @Test
    public void testVisitFrame_Unchanged() {
        Object[] local = new Object[] { Opcodes.INTEGER };
        Object[] stack = new Object[0];
        lvs.changed = false;

        lvs.visitFrame(Opcodes.F_NEW, 1, local, 0, stack);

        verify(mv).visitFrame(Opcodes.F_NEW, 1, local, 0, stack);
    }

    @Test
    public void testVisitFrame_Changed() {
        Object[] local = new Object[] { Opcodes.INTEGER };
        Object[] stack = new Object[0];
        lvs.changed = true;
        lvs.newLocals = new Object[] { Opcodes.INTEGER };
        lvs.mapping = new int[] { 1 };

        Object[] oldLocals = new Object[lvs.newLocals.length];
        System.arraycopy(lvs.newLocals, 0, oldLocals, 0, oldLocals.length);

        lvs.visitFrame(Opcodes.F_NEW, 1, local, 0, stack);

        assertArrayEquals(oldLocals, lvs.newLocals);
        verify(mv).visitFrame(Opcodes.F_NEW, 1, lvs.newLocals, 0, stack);
        assertEquals(oldLocals, lvs.newLocals);
    }

    @Test
    public void testNewLocal() {
        Type type = mock(Type.class);
        when(type.getSort()).thenReturn(1);
        when(type.getSize()).thenReturn(1);

        int local = lvs.newLocal(type);

        assertEquals(lvs.nextLocal, local);
        verify(mv).visitVarInsn(Opcodes.ALOAD, local);
    }

    @Test
    public void testSetLocalType() {
        int local = 1;
        Type type = mock(Type.class);
        lvs.setLocalType(local, type);
    }

    @Test
    public void testSetFrameLocal() {
        int local = 1;
        Object type = Opcodes.INTEGER;
        int length = lvs.newLocals.length;
        if (local >= length) {
            Object[] a = new Object[Math.max(2 * length, local + 1)];
            System.arraycopy(lvs.newLocals, 0, a, 0, length);
            lvs.newLocals = a;
        }
        lvs.newLocals[local] = type;
        assertEquals(type, lvs.newLocals[local]);
    }

    @Test
    public void testRemap_LocalIsBeforeFirstLocal() {
        int var = 1;
        Type type = mock(Type.class);
        int result = lvs.remap(var, type);
        assertEquals(var, result);
    }

    @Test
    public void testRemap_KeyIsGreaterThanMappingLength() {
        int var = 5;
        Type type = mock(Type.class);
        int result = lvs.remap(var, type);
        assertEquals(result, lvs.newLocalMapping(type) - 1);
        assertEquals(result, lvs.newLocalMapping(type) - 1);
        assertEquals(result + 1, lvs.mapping.length);
    }

    @Test
    public void testNewLocalMapping() {
        Type type = mock(Type.class);
        int result = lvs.newLocalMapping(type);
        assertEquals(result, lvs.nextLocal);
        assertEquals(result + type.getSize(), lvs.nextLocal);
    }
}