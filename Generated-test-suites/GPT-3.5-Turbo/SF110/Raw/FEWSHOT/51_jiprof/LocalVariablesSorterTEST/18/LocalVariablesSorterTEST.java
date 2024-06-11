
package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    private MethodVisitor mockMethodVisitor;
    private LocalVariablesSorter localVariablesSorter;

    @Before
    public void setUp() {
        mockMethodVisitor = mock(MethodVisitor.class);
        localVariablesSorter = new LocalVariablesSorter(1, "desc", mockMethodVisitor);
    }

    @Test
    public void testVisitVarInsn() {
        localVariablesSorter.visitVarInsn(Opcodes.ILOAD, 1);
        localVariablesSorter.visitVarInsn(Opcodes.LLOAD, 2);
        localVariablesSorter.visitVarInsn(Opcodes.FLOAD, 3);
        localVariablesSorter.visitVarInsn(Opcodes.DLOAD, 4);
        localVariablesSorter.visitVarInsn(Opcodes.ALOAD, 5);

        verify(mockMethodVisitor).visitVarInsn(Opcodes.ILOAD, 1);
        verify(mockMethodVisitor).visitVarInsn(Opcodes.LLOAD, 2);
        verify(mockMethodVisitor).visitVarInsn(Opcodes.FLOAD, 3);
        verify(mockMethodVisitor).visitVarInsn(Opcodes.DLOAD, 4);
        verify(mockMethodVisitor).visitVarInsn(Opcodes.ALOAD, 5);
    }

    @Test
    public void testVisitIincInsn() {
        localVariablesSorter.visitIincInsn(1, 1);
        localVariablesSorter.visitIincInsn(2, 2);

        verify(mockMethodVisitor).visitIincInsn(1, 1);
        verify(mockMethodVisitor).visitIincInsn(2, 2);
    }

    @Test
    public void testVisitMaxs() {
        localVariablesSorter.visitMaxs(10, 20);

        verify(mockMethodVisitor).visitMaxs(10, localVariablesSorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        Label start = new Label();
        Label end = new Label();

        localVariablesSorter.visitLocalVariable("name1", "desc1", "signature1", start, end, 1);

        verify(mockMethodVisitor).visitLocalVariable("name1", "desc1", "signature1", start, end, localVariablesSorter.remap(1, Type.getType("desc1")));
    }

    @Test
    public void testVisitFrame() {
        Object[] local = new Object[]{Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, "Ljava/lang/Object;"};
        Object[] stack = new Object[]{Opcodes.INTEGER, Opcodes.FLOAT, Opcodes.LONG, Opcodes.DOUBLE, "Ljava/lang/Object;"};

        localVariablesSorter.visitFrame(1, local.length, local, stack.length, stack);

        verify(mockMethodVisitor).visitFrame(1, localVariablesSorter.newLocals.length, localVariablesSorter.newLocals, stack.length, stack);
    }

    @Test
    public void testNewLocal() {
        int local = localVariablesSorter.newLocal(Type.INT_TYPE);

        verify(mockMethodVisitor).visitLocalVariable(anyString(), anyString(), anyString(), any(Label.class), any(Label.class), eq(local));
    }
}
