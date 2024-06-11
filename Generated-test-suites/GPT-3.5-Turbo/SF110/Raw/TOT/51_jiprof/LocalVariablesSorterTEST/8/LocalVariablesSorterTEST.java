import org.junit.Test;
import static org.junit.Assert.*;

public class LocalVariablesSorterTest {
    
    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "()V", null);
        lvs.visitVarInsn(21, 0);
        // Verify that remapping has happened correctly
        assertEquals(1, lvs.remap(0, Type.INT_TYPE));
    }
    
    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "(I)V", null);
        lvs.visitIincInsn(0, 10);
        // Verify that remapping has happened correctly
        assertEquals(1, lvs.remap(0, Type.INT_TYPE));
    }
    
    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "()V", null);
        lvs.visitMaxs(10, 5);
        // Verify that the nextLocal value has been updated correctly
        assertEquals(5, lvs.nextLocal);
    }
    
    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "()V", null);
        lvs.visitLocalVariable("name", "Ljava/lang/String;", null, null, null, 0);
        // Verify that remapping has happened correctly
        assertEquals(1, lvs.remap(0, Type.getObjectType("java/lang/String")));
    }
    
    @Test
    public void testVisitFrame() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "()V", null);
        lvs.visitFrame(0, 2, new Object[] {Opcodes.INTEGER, Opcodes.FLOAT}, 0, null);
        // Verify that remapping and setting frame local has happened correctly
        assertEquals(0, lvs.remap(0, Type.INT_TYPE));
        assertEquals(1, lvs.remap(1, Type.FLOAT_TYPE));
        assertEquals(Opcodes.FLOAT, lvs.newLocals[1]);
    }
    
    @Test
    public void testNewLocal() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "()V", null);
        int local = lvs.newLocal(Type.INT_TYPE);
        // Verify that the nextLocal value has been updated correctly
        assertEquals(1, lvs.nextLocal);
        // Verify that the remapped value is returned
        assertEquals(0, local);
        // Verify that the local type has been set correctly
        assertEquals(Type.INT_TYPE, lvs.getLocalType(local));
    }
    
    @Test
    public void testSetLocalType() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "()V", null);
        lvs.setLocalType(0, Type.DOUBLE_TYPE);
        // Verify that the local type has been set correctly
        assertEquals(Type.DOUBLE_TYPE, lvs.getLocalType(0));
    }
    
    @Test
    public void testSetFrameLocal() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "()V", null);
        lvs.setFrameLocal(0, Opcodes.LONG);
        // Verify that the frame local has been set correctly
        assertEquals(Opcodes.LONG, lvs.newLocals[0]);
    }
    
    @Test
    public void testRemap() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "()V", null);
        int localVar = lvs.newLocal(Type.INT_TYPE);
        int remappedVar = lvs.remap(localVar, Type.INT_TYPE);
        // Verify that the remapped local variable is returned
        assertEquals(0, remappedVar);
    }
    
    @Test
    public void testNewLocalMapping() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0x1, "()V", null);
        int local = lvs.newLocalMapping(Type.OBJECT_TYPE);
        // Verify that the nextLocal value has been updated correctly
        assertEquals(1, lvs.nextLocal);
        // Verify that the remapped value is returned
        assertEquals(0, local);
    }
    
    @Test
    public void testStaticBlock() {
        LocalVariablesSorter lvs = new LocalVariablesSorter(
            0, "()V", null);
        // Test the static block to cover all static initialization
        {
            Type t = lvs.OBJECT_TYPE;
            // Verify that the static variable is correctly initialized
            assertNotNull(t);
        }
    }
}