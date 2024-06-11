
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {
    
    // Test method: visitVarInsn(int, int)
    @Test
    public void testVisitVarInsn() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "", mv);
        lvs.visitVarInsn(21, 1);
        verify(mv).visitVarInsn(21, 1);
    }

    // Test method: visitIincInsn(int, int)
    @Test
    public void testVisitIincInsn() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "", mv);
        lvs.visitIincInsn(1, 1);
        verify(mv).visitIincInsn(1, 1);
    }

    // Test method: visitMaxs(int, int)
    @Test
    public void testVisitMaxs() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "", mv);
        lvs.visitMaxs(10, 20);
        verify(mv).visitMaxs(10, 21);
    }

    // Test method: visitLocalVariable(String, String, String, Label, Label, int)
    @Test
    public void testVisitLocalVariable() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "", mv);
        Label start = new Label();
        Label end = new Label();
        lvs.visitLocalVariable("foo", "I", null, start, end, 1);
        verify(mv).visitLocalVariable("foo", "I", null, start, end, 1);
    }

    // Test method: visitFrame(int, int, Object[], int, Object[])
    @Test(expected = IllegalStateException.class)
    public void testVisitFrame() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "", mv);
        lvs.visitFrame(1, 2, null, 3, null);
    }

    // Test method: newLocal(Type)
    @Test
    public void testNewLocal() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "", mv);
        int local = lvs.newLocal(Type.INT_TYPE);
        assert(lvs.getNextLocal() == local+1);
    }
}