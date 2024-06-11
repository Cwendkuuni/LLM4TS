
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {
    @Test
    public void testVarInsn() {
        // Create mock objects
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)V", mv);

        // Call the method to test
        lvs.visitVarInsn(21, 0);

        // Verify the interactions
        verify(mv).visitVarInsn(21, 0);
    }

    @Test
    public void testVisitIincInsn() {
        // Create mock objects
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)V", mv);

        // Call the method to test
        lvs.visitIincInsn(0, 1);

        // Verify the interactions
        verify(mv).visitIincInsn(0, 1);
    }

    @Test
    public void testVisitMaxs() {
        // Create mock objects
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)V", mv);

        // Call the method to test
        lvs.visitMaxs(1, 2);

        // Verify the interactions
        verify(mv).visitMaxs(1, lvs.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        // Create mock objects
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)V", mv);

        // Call the method to test
        lvs.visitLocalVariable("var", "I", null, null, null, 0);

        // Verify the interactions
        verify(mv).visitLocalVariable("var", "I", null, null, null, lvs.remap(0, Type.INT_TYPE));
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrame() {
        // Create mock objects
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)V", mv);

        // Call the method to test
        lvs.visitFrame(-1, 0, null, 0, null);
    }

    @Test
    public void testNewLocal() {
        // Create mock objects
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)V", mv);

        // Call the method to test
        int local = lvs.newLocal(Type.INT_TYPE);

        // Verify the result
        assertEquals(local, lvs.nextLocal - Type.INT_TYPE.getSize());

        // Verify the interactions
        verify(mv).visitVarInsn(54, local);
    }
}
