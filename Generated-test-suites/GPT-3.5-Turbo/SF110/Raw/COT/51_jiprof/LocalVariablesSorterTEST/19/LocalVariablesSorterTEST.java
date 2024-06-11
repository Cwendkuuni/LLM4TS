import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter sorter;
    private MethodVisitor mockMethodVisitor;

    @Before
    public void setup() {
        mockMethodVisitor = mock(MethodVisitor.class);
        sorter = new LocalVariablesSorter(0,"",mockMethodVisitor);
    }

    @Test
    public void testVisitVarInsn() {
        int opcode = 21; // ILOAD_0
        int var = 0;
        sorter.visitVarInsn(opcode, var);
        verify(mockMethodVisitor).visitVarInsn(opcode, var);
    }

    @Test
    public void testVisitIincInsn() {
        int var = 0;
        int increment = 1;
        sorter.visitIincInsn(var, increment);
        verify(mockMethodVisitor).visitIincInsn(var, increment);
    }

    @Test
    public void testVisitMaxs() {
        int maxStack = 0;
        int maxLocals = sorter.nextLocal;
        sorter.visitMaxs(maxStack, maxLocals);
        verify(mockMethodVisitor).visitMaxs(maxStack, maxLocals);
    }

    @Test
    public void testVisitLocalVariable() {
        String name = "test";
        String desc = "Ljava/lang/String;";
        String signature = null;
        Label start = new Label();
        Label end = new Label();
        int index = 0;
        sorter.visitLocalVariable(name, desc, signature, start, end, index);
        verify(mockMethodVisitor).visitLocalVariable(name, desc, signature, start, end, index);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testVisitFrameWithInvalidType() {
        int type = 1; // INVALID_TYPE
        int nLocal = 0;
        Object[] local = new Object[nLocal];
        int nStack = 0;
        Object[] stack = new Object[nStack];
        sorter.visitFrame(type, nLocal, local, nStack, stack);
    }

    @Test
    public void testNewLocal() {
        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        assert(local >= sorter.firstLocal);
        verify(mockMethodVisitor, times(type.getSize())).visitLocalVariable(anyString(), anyString(), anyString(), any(Label.class), any(Label.class), eq(local));
    }
}