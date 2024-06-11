package org.objectweb.asm.jip.commons;

import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.MethodAdapter;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        int opcode = 1;
        int var = 1;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.visitVarInsn(opcode, var);
    }

    @Test
    public void testVisitIincInsn() {
        int var = 1;
        int increment = 1;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.visitIincInsn(var, increment);
    }

    @Test
    public void testVisitMaxs() {
        int maxStack = 1;
        int maxLocals = 1;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.visitMaxs(maxStack, maxLocals);
    }

    @Test
    public void testVisitLocalVariable() {
        String name = "name";
        String desc = "desc";
        String signature = "signature";
        Label start = new Label();
        Label end = new Label();
        int index = 1;
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Test
    public void testVisitFrame() {
        int type = 1;
        int nLocal = 1;
        Object[] local = new Object[]{"local"};
        int nStack = 1;
        Object[] stack = new Object[]{"stack"};
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.visitFrame(type, nLocal, local, nStack, stack);
    }

    @Test
    public void testNewLocal() {
        Type type = new Type();
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.newLocal(type);
    }

    @Test
    public void testSetLocalType() {
        int local = 1;
        Type type = new Type();
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.setLocalType(local, type);
    }

    @Test
    public void testSetFrameLocal() {
        int local = 1;
        Object type = new Object();
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.setFrameLocal(local, type);
    }

    @Test
    public void testRemap() {
        int var = 1;
        Type type = new Type();
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.remap(var, type);
    }

    @Test
    public void testNewLocalMapping() {
        Type type = new Type();
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(1, "desc", new MethodVisitor(1) {});
        localVariablesSorter.newLocalMapping(type);
    }
}