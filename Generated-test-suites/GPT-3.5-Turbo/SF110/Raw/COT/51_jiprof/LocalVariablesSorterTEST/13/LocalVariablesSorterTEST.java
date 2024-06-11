import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {
    private LocalVariablesSorter localVariablesSorter;
    private MethodVisitor methodVisitor;

    @Before
    public void setUp() {
        methodVisitor = mock(MethodVisitor.class);
        localVariablesSorter = new LocalVariablesSorter(0, "()V", methodVisitor);
    }

    @Test
    public void visitVarInsn_shouldCallVisitVarInsnOnMethodVisitorWithRemappedVariables() {
        localVariablesSorter.visitVarInsn(21, 0);
        verify(methodVisitor).visitVarInsn(21, 1);
    }

    @Test
    public void visitIincInsn_shouldCallVisitIincInsnOnMethodVisitorWithRemappedVariables() {
        localVariablesSorter.visitIincInsn(1, 1);
        verify(methodVisitor).visitIincInsn(1, 1);
    }

    @Test
    public void visitMaxs_shouldCallVisitMaxsOnMethodVisitorWithModifiedMaxLocals() {
        localVariablesSorter.visitMaxs(10, 10);
        verify(methodVisitor).visitMaxs(10, localVariablesSorter.nextLocal);
    }

    @Test
    public void visitLocalVariable_shouldCallVisitLocalVariableOnMethodVisitorWithRemappedIndexes() {
        Label start = new Label();
        Label end = new Label();
        localVariablesSorter.visitLocalVariable("test", "I", null, start, end, 1);
        verify(methodVisitor).visitLocalVariable("test", "I", null, start, end, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void visitFrame_shouldThrowIllegalStateExceptionWhenTypeIsNotMinus1() {
        localVariablesSorter.visitFrame(0, 0, null, 0, null);
    }
  
    @Test
    public void newLocal_shouldReturnNextLocalAndSetTypeAndFrameLocal() {
        int local = localVariablesSorter.newLocal(Type.INT_TYPE);
        assertEquals(local, localVariablesSorter.nextLocal - 1);
        verify(localVariablesSorter).setLocalType(local, Type.INT_TYPE);
        Object obj = localVariablesSorter.newLocals[local - localVariablesSorter.firstLocal];
        assertEquals(Opcodes.INTEGER, obj);
    }
}