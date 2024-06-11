import org.junit.Assert;
import org.junit.Test;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        // Typical use case
        // Test visiting a variable with opcode 21 and var 3
        // This should call mv.visitVarInsn(opcode, remap(var, type))
        // Verify that the remapped value is correctly passed to mv
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        TestMethodVisitor mv = new TestMethodVisitor();
        lvs.mv = mv;
        
        lvs.visitVarInsn(21, 3);
        
        Assert.assertEquals(21, mv.opcode);
        Assert.assertEquals(4, mv.var);
    }
    
    @Test
    public void testVisitIincInsn() {
        // Typical use case
        // Test visiting an incremental instruction with var 5 and increment 10
        // This should call mv.visitIincInsn(remap(var, type), increment)
        // Verify that the remapped value is correctly passed to mv
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        TestMethodVisitor mv = new TestMethodVisitor();
        lvs.mv = mv;
        
        lvs.visitIincInsn(5, 10);
        
        Assert.assertEquals(5, mv.var);
        Assert.assertEquals(10, mv.increment);
    }
    
    @Test
    public void testVisitMaxs() {
        // Typical use case
        // Test visiting max locals with maxStack 20 and maxLocals 10
        // This should call mv.visitMaxs(maxStack, nextLocal)
        // Verify that the nextLocal value is correctly passed to mv
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        TestMethodVisitor mv = new TestMethodVisitor();
        lvs.mv = mv;
        lvs.nextLocal = 20;
        
        lvs.visitMaxs(20, 10);
        
        Assert.assertEquals(20, mv.maxStack);
        Assert.assertEquals(20, mv.nextLocal);
    }
    
    @Test
    public void testVisitLocalVariable() {
        // Typical use case
        // Test visiting a local variable with name "var1", desc "I", signature null, start null, end null, and index 0
        // This should call mv.visitLocalVariable(name, desc, signature, start, end, newIndex) with newIndex as the remapped value of index and desc as the remapped value of Type.getType(desc)
        // Verify that the remapped values are correctly passed to mv
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        TestMethodVisitor mv = new TestMethodVisitor();
        lvs.mv = mv;
        
        lvs.visitLocalVariable("var1", "I", null, null, null, 0);
        
        Assert.assertEquals("var1", mv.name);
        Assert.assertEquals("I", mv.desc);
        Assert.assertNull(mv.signature);
        Assert.assertNull(mv.start);
        Assert.assertNull(mv.end);
        Assert.assertEquals(0, mv.index);
        Assert.assertEquals(0, mv.newIndex);
        Assert.assertNull(mv.type);
    }
    
    @Test
    public void testVisitFrame() {
        // Edge case
        // Test visiting a frame with type -1, nLocal 5, local array {5, 10, null, "Test", null}, nStack 0, and stack array {}
        // This should throw an IllegalStateException since type should be -1
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        TestMethodVisitor mv = new TestMethodVisitor();
        lvs.mv = mv;
        
        try {
            lvs.visitFrame(-1, 5, new Object[]{5, 10, null, "Test", null}, 0, new Object[]{});
            Assert.fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testNewLocal() {
        // Typical use case
        // Test creating a new local variable with type int
        // This should call setFrameLocal(local, type.getSort()) and return the local variable
        // Verify that the local variable is correctly returned
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        lvs.nextLocal = 3;
        
        int local = lvs.newLocal(Type.INT_TYPE);
        
        Assert.assertEquals(3, local);
        Assert.assertEquals(Opcodes.INTEGER, lvs.newLocals[3]);
    }
    
    @Test
    public void testSetLocalType() {
        // Error scenario
        // Test setting the local type of a variable
        // Since the code for this method is empty, there is no functionality to test
        // Just ensure that no exceptions are thrown        
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        
        lvs.setLocalType(0, Type.INT_TYPE);
    }
    
    @Test
    public void testSetFrameLocal() {
        // Typical use case
        // Test setting the frame local at index 5 to type DOUBLE
        // Call the setFrameLocal method and verify that the value is correctly set at index 5 in the newLocals array
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        lvs.newLocals = new Object[10];
        
        lvs.setFrameLocal(5, Opcodes.DOUBLE);
        
        Assert.assertEquals(Opcodes.DOUBLE, lvs.newLocals[5]);
    }
    
    @Test
    public void testRemap() {
        // Typical use case
        // Test remapping a variable with var as 2 and type as Type.DOUBLE_TYPE
        // Since it's the typical use case, the var is greater than firstLocal
        // After calling remap, the first local mapping should be created, the local type should be set, and the remapped value should be returned
        // Verify that the remapped value is correctly returned and the mapping, local type, and changed flag are correctly updated
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        lvs.firstLocal = 1;
        lvs.nextLocal = 3;
        
        int remapped = lvs.remap(2, Type.DOUBLE_TYPE);
        
        Assert.assertEquals(3, remapped);
        Assert.assertEquals(3, lvs.mapping[3]);
        Assert.assertEquals(Type.DOUBLE_TYPE, lvs.newLocals[3]);
        Assert.assertTrue(lvs.changed);
    }
    
    @Test
    public void testNewLocalMapping() {
        // Typical use case
        // Test creating a new local mapping for a variable with type LONG
        // The nextLocal should be increased to 3 and the new local mapping (2) should be returned
        // Verify that the correct local mapping and nextLocal values are returned
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "(I)I", null);
        lvs.nextLocal = 2;
        
        int local = lvs.newLocalMapping(Type.LONG_TYPE);
        
        Assert.assertEquals(2, local);
        Assert.assertEquals(3, lvs.nextLocal);
    }
    
    private class TestMethodVisitor extends MethodVisitor {
        int opcode;
        int var;
        int increment;
        int maxStack;
        int nextLocal;
        String name;
        String desc;
        String signature;
        Label start;
        Label end;
        int index;
        int newIndex;
        Object type;
        
        public TestMethodVisitor() {
            super(0);
        }
        
        @Override
        public void visitVarInsn(int opcode, int var) {
            this.opcode = opcode;
            this.var = var;
        }
        
        @Override
        public void visitIincInsn(int var, int increment) {
            this.var = var;
            this.increment = increment;
        }
        
        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            this.maxStack = maxStack;
            this.nextLocal = maxLocals;
        }
        
        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            this.name = name;
            this.desc = desc;
            this.signature = signature;
            this.start = start;
            this.end = end;
            this.index = index;
            this.newIndex = LocalVariablesSorterTest.this.lvs.remap(index, Type.getType(desc));
            this.type = LocalVariablesSorterTest.this.lvs.newLocals[newIndex];
        }
        
        @Override
        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            throw new IllegalStateException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
        }
    }
}