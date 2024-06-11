package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.objectweb.asm.*;
import org.objectweb.asm.jip.MethodAdapter;

public class LocalVariablesSorterTest {
    private LocalVariablesSorter localVariablesSorter;

    @Test
    public void testConstructor() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);

        assertEquals(40, localVariablesSorter.mapping.length);
        assertEquals(20, localVariablesSorter.newLocals.length);
        assertEquals(0, localVariablesSorter.nextLocal);
        assertEquals(1, localVariablesSorter.firstLocal);
    }

    @Test
    public void testVisitVarInsn() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        Type.LONG_TYPE = Type.getObjectType("long");
        localVariablesSorter.mv = mv;

        localVariablesSorter.visitVarInsn(22, 1);
        localVariablesSorter.visitVarInsn(55, 2);
        localVariablesSorter.visitVarInsn(24, 3);
        localVariablesSorter.visitVarInsn(57, 4);
        localVariablesSorter.visitVarInsn(23, 5);
        localVariablesSorter.visitVarInsn(56, 6);
        localVariablesSorter.visitVarInsn(21, 7);
        localVariablesSorter.visitVarInsn(54, 8);

        // Assertions
        // TODO: Add assertions for all cases
    }

    @Test
    public void testVisitIincInsn() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        localVariablesSorter.mv = mv;

        localVariablesSorter.visitIincInsn(1, 5);

        // Assertions
        // TODO: Add assertions
    }

    @Test
    public void testVisitMaxs() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        localVariablesSorter.mv = mv;

        localVariablesSorter.visitMaxs(5, 10);

        // Assertions
        // TODO: Add assertions
    }

    @Test
    public void testVisitLocalVariable() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        localVariablesSorter.mv = mv;

        Label start = new Label();
        Label end = new Label();
        localVariablesSorter.visitLocalVariable("name", "desc", "signature", start, end, 1);

        // Assertions
        // TODO: Add assertions
    }

    @Test
    public void testVisitFrame() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        localVariablesSorter.changed = true;
        localVariablesSorter.mv = mv;

        Object[] local = new Object[5];
        Object[] stack = new Object[10];
        localVariablesSorter.visitFrame(-1, 5, local, 10, stack);

        // Assertions
        // TODO: Add assertions
    }

    @Test
    public void testNewLocal() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        localVariablesSorter.mv = mv;
        Type.OBJECT_TYPE = Type.getObjectType("java/lang/Object");

        int local = localVariablesSorter.newLocal(Type.INT_TYPE);
        assertEquals(1, localVariablesSorter.nextLocal);
        // TODO: Add more assertions
    }

    @Test
    public void testSetLocalType() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        localVariablesSorter.mv = mv;

        localVariablesSorter.setLocalType(1, Type.INT_TYPE);

        // Assertions
        // TODO: Add assertions
    }

    @Test
    public void testSetFrameLocal() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        localVariablesSorter.mv = mv;

        localVariablesSorter.setFrameLocal(1, Type.INT_TYPE);

        // Assertions
        // TODO: Add assertions
    }

    @Test
    public void testRemap() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        localVariablesSorter.mv = mv;

        int remap = localVariablesSorter.remap(1, Type.INT_TYPE);

        // Assertions
        // TODO: Add assertions
    }

    @Test
    public void testNewLocalMapping() {
        MethodVisitor mv = new MethodAdapter(null) {};
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
        localVariablesSorter.mv = mv;

        int newLocalMapping = localVariablesSorter.newLocalMapping(Type.INT_TYPE);

        // Assertions
        // TODO: Add assertions
    }

    @Test
    public void testStaticBlock() {
        assertEquals(Type.getObjectType("java/lang/Object"), LocalVariablesSorter.OBJECT_TYPE);
    }
}