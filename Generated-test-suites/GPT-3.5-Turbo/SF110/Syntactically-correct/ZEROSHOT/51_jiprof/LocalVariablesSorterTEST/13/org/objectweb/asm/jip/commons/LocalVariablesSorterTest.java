
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter localVariablesSorter;
    private MethodVisitor methodVisitor;

    @Before
    public void setUp() {
        methodVisitor = mock(MethodVisitor.class);
        localVariablesSorter = new LocalVariablesSorter(0, "", methodVisitor);
    }

    @Test
    public void testVisitVarInsnWithLongType() {
        localVariablesSorter.visitVarInsn(22, 1);
        verify(methodVisitor, times(1)).visitVarInsn(22, 1);
    }

    @Test
    public void testVisitVarInsnWithDoubleType() {
        localVariablesSorter.visitVarInsn(24, 1);
        verify(methodVisitor, times(1)).visitVarInsn(24, 1);
    }

    @Test
    public void testVisitVarInsnWithFloatType() {
        localVariablesSorter.visitVarInsn(23, 1);
        verify(methodVisitor, times(1)).visitVarInsn(23, 1);
    }

    @Test
    public void testVisitVarInsnWithIntType() {
        localVariablesSorter.visitVarInsn(21, 1);
        verify(methodVisitor, times(1)).visitVarInsn(21, 1);
    }

    @Test
    public void testVisitIincInsn() {
        localVariablesSorter.visitIincInsn(1, 1);
        verify(methodVisitor, times(1)).visitIincInsn(1, 1);
    }

    @Test
    public void testVisitMaxs() {
        localVariablesSorter.visitMaxs(1, 1);
        verify(methodVisitor, times(1)).visitMaxs(1, localVariablesSorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        localVariablesSorter.visitLocalVariable("name", "desc", "signature", null, null, 1);
        verify(methodVisitor, times(1)).visitLocalVariable("name", "desc", "signature", null, null, 1);
    }

    @Test
    public void testVisitFrame() {
        localVariablesSorter.visitFrame(0, 0, null, 0, null);
        verify(methodVisitor, times(1)).visitFrame(0, 0, null, 0, null);
    }

    @Test
    public void testNewLocal() {
        Type type = Type.getType("I");
        int local = localVariablesSorter.newLocal(type);
        verify(methodVisitor, times(1)).visitVarInsn(eq(21), eq(local));
        verify(methodVisitor, times(1)).visitVarInsn(eq(54), eq(local));

        type = Type.getType("Ljava/lang/String;");
        local = localVariablesSorter.newLocal(type);
        verify(methodVisitor, times(1)).visitVarInsn(eq(21), eq(local));
        verify(methodVisitor, times(1)).visitVarInsn(eq(58), eq(local));
    }
}
