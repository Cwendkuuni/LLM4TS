package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {
    private LocalVariablesSorter localVariablesSorter;
    private MethodVisitor methodVisitor;

    @Before
    public void setUp() {
        methodVisitor = mock(MethodVisitor.class);
        localVariablesSorter = new LocalVariablesSorter(1, "Ljava/lang/String;", methodVisitor);
    }

    @Test
    public void testVisitVarInsn() {
        localVariablesSorter.visitVarInsn(22, 1);
        verify(methodVisitor).visitVarInsn(22, 1);
        localVariablesSorter.visitVarInsn(55, 2);
        verify(methodVisitor).visitVarInsn(55, 2);
        localVariablesSorter.visitVarInsn(24, 3);
        verify(methodVisitor).visitVarInsn(24, 3);
        localVariablesSorter.visitVarInsn(57, 4);
        verify(methodVisitor).visitVarInsn(57, 4);
        localVariablesSorter.visitVarInsn(23, 5);
        verify(methodVisitor).visitVarInsn(23, 5);
        localVariablesSorter.visitVarInsn(56, 6);
        verify(methodVisitor).visitVarInsn(56, 6);
        localVariablesSorter.visitVarInsn(21, 7);
        verify(methodVisitor).visitVarInsn(21, 7);
        localVariablesSorter.visitVarInsn(54, 8);
        verify(methodVisitor).visitVarInsn(54, 8);
        localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 9);
        verify(methodVisitor).visitVarInsn(Opcodes.ILOAD, 9);
    }

    @Test
    public void testVisitIincInsn() {
        localVariablesSorter.visitIincInsn(1, 2);
        verify(methodVisitor).visitIincInsn(1, 2);
        localVariablesSorter.visitIincInsn(3, 4);
        verify(methodVisitor).visitIincInsn(3, 4);
        localVariablesSorter.visitIincInsn(5, 6);
        verify(methodVisitor).visitIincInsn(5, 6);
        localVariablesSorter.visitIincInsn(7, 8);
        verify(methodVisitor).visitIincInsn(7, 8);
        localVariablesSorter.visitIincInsn(9, 10);
        verify(methodVisitor).visitIincInsn(9, 10);
    }

    @Test
    public void testVisitMaxs() {
        localVariablesSorter.visitMaxs(1, 2);
        verify(methodVisitor).visitMaxs(1, localVariablesSorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        String name = "var";
        String desc = "Lorg/test/Class;";
        String signature = "Lorg/test/Class;";
        org.objectweb.asm.Label start = mock(org.objectweb.asm.Label.class);
        org.objectweb.asm.Label end = mock(org.objectweb.asm.Label.class);
        int index = 1;
        localVariablesSorter.visitLocalVariable(name, desc, signature, start, end, index);
        int newIndex = localVariablesSorter.remap(index, Type.getType(desc));
        verify(methodVisitor).visitLocalVariable(name, desc, signature, start, end, newIndex);
    }

    @Test
    public void testVisitFrame() {
        int type = -1;
        int nLocal = 1;
        Object[] local = new Object[]{1};
        int nStack = 2;
        Object[] stack = new Object[]{2};
        localVariablesSorter.visitFrame(type, nLocal, local, nStack, stack);
        verify(methodVisitor).visitFrame(type, 1, localVariablesSorter.newLocals, nStack, stack);
    }

    @Test
    public void testNewLocal() {
        Type type = Type.INT_TYPE;
        int expectedLocal = localVariablesSorter.nextLocal;
        localVariablesSorter.nextLocal += type.getSize();
        int local = localVariablesSorter.newLocal(type);
        localVariablesSorter.setLocalType(expectedLocal, type);
        localVariablesSorter.setFrameLocal(expectedLocal, Opcodes.INTEGER);
        assert(local == expectedLocal);
    }

    @Test
    public void testSetLocalType() {
        int local = 1;
        Type type = Type.INT_TYPE;
        localVariablesSorter.setLocalType(local, type);
        // verify that the setLocalType method does not contain any logic to be tested
    }

    @Test
    public void testSetFrameLocal() {
        int local = 1;
        Object type = Opcodes.INTEGER;
        localVariablesSorter.setFrameLocal(local, type);
        assert(localVariablesSorter.newLocals[local] == type);
    }

    @Test
    public void testRemap() {
        int var = 1;
        Type type = Type.FLOAT_TYPE;
        localVariablesSorter.mapping[2 * var + type.getSize() - 1] = localVariablesSorter.newLocalMapping(type) + 1;
        localVariablesSorter.changed = false;
        int expectedValue = localVariablesSorter.mapping[2 * var + type.getSize() - 1] - 1;
        int value = localVariablesSorter.remap(var, type);
        assert(value == expectedValue && localVariablesSorter.changed);
    }

    @Test
    public void testNewLocalMapping() {
        Type type = Type.INT_TYPE;
        int expectedLocal = localVariablesSorter.nextLocal;
        localVariablesSorter.nextLocal += type.getSize();
        int local = localVariablesSorter.newLocalMapping(type);
        assert(local == expectedLocal);
    }

}