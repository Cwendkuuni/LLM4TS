
package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.*;
import org.objectweb.asm.jip.*;

import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    private MethodVisitor methodVisitor;
    private LocalVariablesSorter localVariablesSorter;

    @Before
    public void setUp() {
        methodVisitor = mock(MethodVisitor.class);
        localVariablesSorter = new LocalVariablesSorter(1, "(II)V", methodVisitor);
    }

    @Test
    public void testVisitVarInsn() {
        localVariablesSorter.visitVarInsn(21, 0);
        verify(methodVisitor).visitVarInsn(21, 0);
    }

    @Test
    public void testVisitIincInsn() {
        localVariablesSorter.visitIincInsn(1, 1);
        verify(methodVisitor).visitIincInsn(1, 1);
    }

    @Test
    public void testVisitMaxs() {
        localVariablesSorter.visitMaxs(3, 3);
        verify(methodVisitor).visitMaxs(3, 2);
    }

    @Test
    public void testVisitLocalVariable() {
        Label start = new Label();
        Label end = new Label();
        Type type = Type.INT_TYPE;
        localVariablesSorter.visitLocalVariable("var", type.getDescriptor(), null, start, end, 0);
        verify(methodVisitor).visitLocalVariable("var", type.getDescriptor(), null, start, end, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrame() {
        localVariablesSorter.visitFrame(1, 1, new Object[1], 1, new Object[1]);
    }

    @Test
    public void testNewLocal() {
        Type type = Type.INT_TYPE;
        localVariablesSorter.newLocal(type);
        verify(methodVisitor).visitVarInsn(54, 2);
    }
}
