import org.objectweb.asm.Type;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.Label;
import org.objectweb.asm.MethodVisitor;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);
        try {
            sorter.visitFrame(-1, 2, new Object[]{"I", "J"}, 1, new Object[]{"Ljava/lang/String;"});
            fail("Expected IllegalStateException not thrown");
        } catch (IllegalStateException e) {
            // Pass
        }
    }

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);
        
        sorter.visitVarInsn(21, 0);
        assertEquals(0, sorter.remap(0, Type.INT_TYPE));

        sorter.visitVarInsn(22, 1);
        assertEquals(1, sorter.remap(1, Type.LONG_TYPE));

        sorter.visitVarInsn(23, 2);
        assertEquals(2, sorter.remap(2, Type.FLOAT_TYPE));

        sorter.visitVarInsn(24, 3);
        assertEquals(3, sorter.remap(3, Type.DOUBLE_TYPE));

        sorter.visitVarInsn(25, 4);
        assertEquals(4, sorter.remap(4, LocalVariablesSorter.OBJECT_TYPE));
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);
        sorter.visitLocalVariable("var1", "I", null, new Label(), new Label(), 0);
        assertEquals(0, sorter.remap(0, Type.INT_TYPE));
    }

}