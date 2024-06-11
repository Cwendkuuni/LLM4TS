import org.junit.Test;
import static org.junit.Assert.*;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.Type;


public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        try {
            LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor());
            assertNotNull(sorter);
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testVisitVarInsn() {
        try {
            LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor());
            sorter.visitVarInsn(21, 1);
            assertEquals(1, sorter.getNextLocal());

            sorter.visitVarInsn(22, 2);
            assertEquals(2, sorter.getNextLocal());

            sorter.visitVarInsn(23, 3);
            assertEquals(3, sorter.getNextLocal());

            sorter.visitVarInsn(24, 4);
            assertEquals(4, sorter.getNextLocal());

            sorter.visitVarInsn(54, 5);
            assertEquals(5, sorter.getNextLocal());

            sorter.visitVarInsn(55, 6);
            assertEquals(6, sorter.getNextLocal());
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testVisitIincInsn() {
        try {
            LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor());
            sorter.visitIincInsn(1, 1);
            assertTrue(true);
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testVisitMaxs() {
        try {
            LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor());
            sorter.visitMaxs(5, 10);
            assertTrue(true);
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testVisitLocalVariable() {
        try {
            LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor());
            sorter.visitLocalVariable("name", "desc", "signature", new Label(), new Label(), 1);
            assertTrue(true);
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testVisitFrame() {
        try {
            LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor());
            sorter.visitFrame(1, 1, new Object[] {"obj"}, 0, new Object[] {});
            assertTrue(true);
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNewLocal() {
        try {
            LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor());
            int local = sorter.newLocal(Type.INT_TYPE);
            assertEquals(1, local);
            assertEquals(2, sorter.getNextLocal());

            local = sorter.newLocal(Type.FLOAT_TYPE);
            assertEquals(2, local);
            assertEquals(3, sorter.getNextLocal());

            local = sorter.newLocal(Type.LONG_TYPE);
            assertEquals(3, local);
            assertEquals(5, sorter.getNextLocal());

            local = sorter.newLocal(Type.DOUBLE_TYPE);
            assertEquals(5, local);
            assertEquals(7, sorter.getNextLocal());

            local = sorter.newLocal(Type.getObjectType("java/lang/Object"));
            assertEquals(7, local);
            assertEquals(8, sorter.getNextLocal());
        } catch(Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}