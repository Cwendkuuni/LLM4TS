package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LocalVariablesSorterTest {
    @Test
    public void testConstructor() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        assertNotNull(sorter.mapping);
        assertNotNull(sorter.newLocals);
        assertEquals(40, sorter.mapping.length);
        assertEquals(20, sorter.newLocals.length);
        assertEquals(1, sorter.nextLocal);
        assertEquals(1, sorter.firstLocal);
        assertNull(sorter.changed);
        verify(mv).visitVarInsn(0, sorter.remap(0, null));
    }

    @Test
    public void testVisitVarInsn() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        sorter.visitVarInsn(22, 0);
        verify(mv).visitVarInsn(22, 2);

        sorter.visitVarInsn(55, 1);
        verify(mv).visitVarInsn(55, 3);

        sorter.visitVarInsn(24, 2);
        verify(mv).visitVarInsn(24, 4);

        sorter.visitVarInsn(57, 3);
        verify(mv).visitVarInsn(57, 5);

        sorter.visitVarInsn(23, 4);
        verify(mv).visitVarInsn(23, 6);

        sorter.visitVarInsn(56, 5);
        verify(mv).visitVarInsn(56, 7);

        sorter.visitVarInsn(21, 6);
        verify(mv).visitVarInsn(21, 8);

        sorter.visitVarInsn(54, 7);
        verify(mv).visitVarInsn(54, 9);

        sorter.visitVarInsn(18, 8);
        verify(mv).visitVarInsn(18, 10);
    }

    @Test
    public void testVisitIincInsn() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        sorter.visitIincInsn(0, 10);
        verify(mv).visitIincInsn(0, 0);

        sorter.visitIincInsn(1, 5);
        verify(mv).visitIincInsn(1, 1);
    }

    @Test
    public void testVisitMaxs() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        sorter.visitMaxs(10, 20);
        verify(mv).visitMaxs(10, 1);
    }

    @Test
    public void testVisitLocalVariable() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        Label start = new Label();
        Label end = new Label();

        sorter.visitLocalVariable("name", "desc", "signature", start, end, 0);
        verify(mv).visitLocalVariable("name", "desc", "signature", start, end, 0);
    }

    @Test
    public void testVisitFrame() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        Object[] local = new Object[] {"Opcodes.INTEGER"};
        Object[] stack = new Object[] {"Opcodes.TOP"};
        sorter.visitFrame(0, 1, local, 1, stack);
        verify(mv).visitFrame(0, 1, local, 1, stack);
    }

    @Test
    public void testNewLocal() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        int local = 0;
        Type type = Type.INT_TYPE;
        int result = sorter.newLocal(type);
        verify(mv).visitVarInsn(0, result);
    }

    @Test
    public void testSetLocalType() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        int local = 0;
        Type type = Type.INT_TYPE;
        sorter.setLocalType(local, type);
    }

    @Test
    public void testSetFrameLocal() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        int local = 0;
        Object type = "Opcodes.INTEGER";

        sorter.newLocals = new Object[1];
        sorter.setFrameLocal(local, type);
        assertEquals(type, sorter.newLocals[0]);

        sorter.newLocals = new Object[0];
        sorter.setFrameLocal(local, type);
        assertNull(sorter.newLocals[0]);
    }

    @Test
    public void testRemap() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        int var = 0;
        Type type = null;
        int result = sorter.remap(var, type);
        assertEquals(var, result);
    }

    @Test
    public void testNewLocalMapping() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "desc", mv);

        Type type = Type.INT_TYPE;
        int result = sorter.newLocalMapping(type);
        assertEquals(sorter.nextLocal, result);
    }
}