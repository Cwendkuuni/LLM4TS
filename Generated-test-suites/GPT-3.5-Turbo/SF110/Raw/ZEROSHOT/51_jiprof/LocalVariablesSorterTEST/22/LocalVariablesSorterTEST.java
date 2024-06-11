import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.MethodVisitor;

import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter localVariablesSorter;
    private MethodVisitor methodVisitor;

    @Before
    public void setUp() {
        methodVisitor = mock(MethodVisitor.class);
        localVariablesSorter = new LocalVariablesSorter(0, "methodDesc", methodVisitor);
    }

    @Test
    public void testVisitVarInsn() {
        localVariablesSorter.visitVarInsn(21, 1);
        verify(methodVisitor).visitVarInsn(21, 1);

        localVariablesSorter.visitVarInsn(22, 2);
        verify(methodVisitor).visitVarInsn(22, 2);

        localVariablesSorter.visitVarInsn(23, 3);
        verify(methodVisitor).visitVarInsn(23, 3);

        localVariablesSorter.visitVarInsn(24, 4);
        verify(methodVisitor).visitVarInsn(24, 4);

        localVariablesSorter.visitVarInsn(54, 5);
        verify(methodVisitor).visitVarInsn(54, 5);

        localVariablesSorter.visitVarInsn(55, 6);
        verify(methodVisitor).visitVarInsn(55, 6);

        localVariablesSorter.visitVarInsn(56, 7);
        verify(methodVisitor).visitVarInsn(56, 7);

        localVariablesSorter.visitVarInsn(57, 8);
        verify(methodVisitor).visitVarInsn(57, 8);

        localVariablesSorter.visitVarInsn(58, 9);
        verify(methodVisitor).visitVarInsn(58, 9);
    }

    @Test
    public void testVisitIincInsn() {
        localVariablesSorter.visitIincInsn(1, 5);
        verify(methodVisitor).visitIincInsn(1, 5);
    }

    @Test
    public void testVisitMaxs() {
        localVariablesSorter.visitMaxs(5, 10);
        verify(methodVisitor).visitMaxs(5, localVariablesSorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        localVariablesSorter.visitLocalVariable("name", "desc", "signature", null, null, 1);
        verify(methodVisitor).visitLocalVariable("name", "desc", "signature", null, null, localVariablesSorter.remap(1, Type.getType("desc")));
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrame() {
        localVariablesSorter.visitFrame(-1, 0, new Object[0], 0, new Object[0]);
    }

    @Test
    public void testNewLocal() {
        Type type = mock(Type.class);
        localVariablesSorter.newLocal(type);
        int expectedLocal = localVariablesSorter.nextLocal - type.getSize();
        verify(localVariablesSorter).setLocalType(expectedLocal, type);
        verify(localVariablesSorter).setFrameLocal(expectedLocal, type);
    }

    @Test
    public void testSetLocalType() {
        localVariablesSorter.setLocalType(1, Type.INT_TYPE);

        // Verify implementation
    }

    @Test
    public void testSetFrameLocal() {
        localVariablesSorter.setFrameLocal(1, Type.INT_TYPE);

        // Verify implementation
    }

    @Test
    public void testRemap() {
        int var = 1;
        Type type = Type.INT_TYPE;
        int remappedVar = localVariablesSorter.remap(var, type);
        verify(localVariablesSorter).newLocalMapping(type);
        verify(localVariablesSorter).setLocalType(remappedVar, type);
        verify(localVariablesSorter).setFrameLocal(remappedVar, type);

        // Verify implementation

    }

    @Test
    public void testNewLocalMapping() {
        Type type = mock(Type.class);
        int local = localVariablesSorter.nextLocal;
        localVariablesSorter.newLocalMapping(type);
        verify(localVariablesSorter).setLocalType(local, type);
        localVariablesSorter.nextLocal += type.getSize();
    }

}