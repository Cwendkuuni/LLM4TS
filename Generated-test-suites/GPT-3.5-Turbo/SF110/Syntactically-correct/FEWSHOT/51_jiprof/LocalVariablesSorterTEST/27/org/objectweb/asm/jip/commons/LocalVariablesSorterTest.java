
package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodAdapter;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    private MethodVisitor mv;
    private LocalVariablesSorter localVariablesSorter;

    @Before
    public void setUp() {
        mv = mock(MethodVisitor.class);
        localVariablesSorter = new LocalVariablesSorter(0, "()V", mv);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, localVariablesSorter.mapping.length);
        assertEquals(0, localVariablesSorter.newLocals.length);
        assertEquals(1, localVariablesSorter.nextLocal);

        Type[] args = Type.getArgumentTypes("()V");
        assertEquals(1, localVariablesSorter.firstLocal);

        localVariablesSorter = new LocalVariablesSorter(1, "(Ljava/lang/String;)V", mv);
        assertEquals(1, localVariablesSorter.nextLocal);

        args = Type.getArgumentTypes("(Ljava/lang/String;)V");
        assertEquals(2, localVariablesSorter.firstLocal);
    }

    @Test
    public void testVisitVarInsn() {
        localVariablesSorter.mv = mv;

        localVariablesSorter.visitVarInsn(21, 0);
        verify(mv).visitVarInsn(21, 0);

        localVariablesSorter.visitVarInsn(22, 1);
        verify(mv).visitVarInsn(22, 1);

        localVariablesSorter.visitVarInsn(23, 2);
        verify(mv).visitVarInsn(23, 2);

        localVariablesSorter.visitVarInsn(24, 3);
        verify(mv).visitVarInsn(24, 3);

        localVariablesSorter.visitVarInsn(25, 4);
        verify(mv).visitVarInsn(25, 4);
    }

    @Test
    public void testVisitIincInsn() {
        localVariablesSorter.mv = mv;

        localVariablesSorter.visitIincInsn(0, 1);
        verify(mv).visitIincInsn(0, 1);
    }

    @Test
    public void testVisitMaxs() {
        localVariablesSorter.mv = mv;

        localVariablesSorter.visitMaxs(5, 6);
        verify(mv).visitMaxs(5, localVariablesSorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        localVariablesSorter.mv = mv;

        Label start = mock(Label.class);
        Label end = mock(Label.class);

        localVariablesSorter.visitLocalVariable("name", "desc", "signature",
                start, end, 0);
        verify(mv).visitLocalVariable("name", "desc", "signature",
                start, end, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrameWithInvalidType() {
        localVariablesSorter.mv = mv;

        localVariablesSorter.visitFrame(1, 2, new Object[2], 3, new Object[3]);
    }

    @Test
    public void testVisitFrame() {
        localVariablesSorter.mv = mv;

        localVariablesSorter.changed = false;

        Object[] oldLocals = new Object[2];
        Object[] newLocals = new Object[5];
        System.arraycopy(oldLocals, 0, newLocals, 0, oldLocals.length);

        Field newLocalsField = null;
        try {
            newLocalsField = localVariablesSorter.getClass().getDeclaredField("newLocals");
            newLocalsField.setAccessible(true);
            newLocalsField.set(localVariablesSorter, newLocals);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        Field changedField = null;
        try {
            changedField = localVariablesSorter.getClass().getDeclaredField("changed");
            changedField.setAccessible(true);
            changedField.set(localVariablesSorter, true);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        localVariablesSorter.visitFrame(-1, 2, new Object[]{1, 1}, 3, new Object[]{2, 2, 2});

        verify(mv).visitFrame(-1, 1, newLocals, 3, new Object[]{2, 2, 2});

        Field mappingField = null;
        try {
            mappingField = localVariablesSorter.getClass().getDeclaredField("mapping");
            mappingField.setAccessible(true);
            mappingField.set(localVariablesSorter, new int[2]);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        newLocals = new Object[]{null, Type.INT_TYPE};
        try {
            newLocalsField.set(localVariablesSorter, newLocals);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        localVariablesSorter.visitFrame(-1, 2, new Object[]{Opcodes.INTEGER, Opcodes.INTEGER}, 3, new Object[]{2, 2, 2});

        verify(mv).visitFrame(-1, 1, newLocals, 3, new Object[]{2, 2, 2});

        changedField.setAccessible(false);
    }

    @Test
    public void testNewLocal() {
        localVariablesSorter.mv = mv;

        int local = localVariablesSorter.newLocal(Type.INT_TYPE);
        assertEquals(1, localVariablesSorter.nextLocal);
        assertEquals(2, local);
        assertEquals(Opcodes.INTEGER, localVariablesSorter.newLocals[local]);
        assertEquals(null, localVariablesSorter.newLocals[0]);

        local = localVariablesSorter.newLocal(Type.FLOAT_TYPE);
        assertEquals(3, localVariablesSorter.nextLocal);
        assertEquals(3, local);
        assertEquals(Opcodes.FLOAT, localVariablesSorter.newLocals[local]);
        assertEquals(null, localVariablesSorter.newLocals[2]);

        local = localVariablesSorter.newLocal(Type.LONG_TYPE);
        assertEquals(5, localVariablesSorter.nextLocal);
        assertEquals(4, local);
        assertEquals(Opcodes.LONG, localVariablesSorter.newLocals[local]);
        assertEquals(null, localVariablesSorter.newLocals[4]);

        local = localVariablesSorter.newLocal(Type.DOUBLE_TYPE);
        assertEquals(7, localVariablesSorter.nextLocal);
        assertEquals(6, local);
        assertEquals(Opcodes.DOUBLE, localVariablesSorter.newLocals[local]);
        assertEquals(null, localVariablesSorter.newLocals[6]);

        local = localVariablesSorter.newLocal(Type.getType("Ljava/lang/Object;"));
        assertEquals(8, localVariablesSorter.nextLocal);
        assertEquals(7, local);
        assertEquals(Type.getType("Ljava/lang/Object;"), localVariablesSorter.newLocals[local]);
        assertEquals(null, localVariablesSorter.newLocals[8]);

        local = localVariablesSorter.newLocal(Type.getType("[Ljava/lang/Object;"));
        assertEquals(9, localVariablesSorter.nextLocal);
        assertEquals(8, local);
        assertEquals(Type.getType("[Ljava/lang/Object;"), localVariablesSorter.newLocals[local]);
        assertEquals(null, localVariablesSorter.newLocals[9]);
    }

    @Test
    public void testSetLocalType() {
        localVariablesSorter.setLocalType(0, Type.INT_TYPE);

        assertEquals(null, localVariablesSorter.newLocals[0]);
    }

    @Test
    public void testSetFrameLocal() {
        localVariablesSorter.setFrameLocal(0, Type.INT_TYPE);

        assertEquals(Opcodes.INTEGER, localVariablesSorter.newLocals[0]);
    }

    @Test
    public void testRemapWhenVarIsLessThanFirstLocal() {
        int remappedVar = localVariablesSorter.remap(0, Type.INT_TYPE);

        assertEquals(0, remappedVar);
        assertFalse(localVariablesSorter.changed);
    }

    @Test
    public void testRemapWhenVarIsGreaterThanEqualFirstLocal() {
        localVariablesSorter.mapping = new int[2];
        localVariablesSorter.newLocalMapping(Type.INT_TYPE);

        int remappedVar = localVariablesSorter.remap(1, Type.INT_TYPE);

        assertEquals(1, remappedVar);
        assertTrue(localVariablesSorter.changed);
    }

    @Test
    public void testNewLocalMappingIntType() {
        int local = localVariablesSorter.newLocalMapping(Type.INT_TYPE);

        assertEquals(1, localVariablesSorter.nextLocal);
        assertEquals(1, local);
    }
}
