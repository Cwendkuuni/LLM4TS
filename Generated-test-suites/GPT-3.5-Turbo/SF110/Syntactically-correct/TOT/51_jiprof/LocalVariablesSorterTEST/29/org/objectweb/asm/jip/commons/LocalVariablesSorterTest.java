package org.objectweb.asm.jip.commons.test;

import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.jip.LocalVariablesSorter;
import org.objectweb.asm.jip.MethodVisitor;
import org.objectweb.asm.jip.Type;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        // Typical case
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {});
        Assert.assertNotNull(sorter);
        
        // Edge case: minimum access
        sorter = new LocalVariablesSorter(0, "desc", new MethodVisitor() {});
        Assert.assertNotNull(sorter);
        
        // Edge case: empty descriptor
        sorter = new LocalVariablesSorter(1, "", new MethodVisitor() {});
        Assert.assertNotNull(sorter);
    }
    
    @Test
    public void testVisitVarInsn() {
        // Typical case
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitVarInsn(int opcode, int var) {
                Assert.assertEquals(1, opcode);
                Assert.assertEquals(1, var);
            }
        });
        sorter.visitVarInsn(1, 1);
        
        // Edge case: opcode 0
        sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitVarInsn(int opcode, int var) {
                Assert.assertEquals(0, opcode);
                Assert.assertEquals(2, var);
            }
        });
        sorter.visitVarInsn(0, 2);
    }
    
    @Test
    public void testVisitIincInsn() {
        // Typical case
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitIincInsn(int var, int increment) {
                Assert.assertEquals(2, var);
                Assert.assertEquals(5, increment);
            }
        });
        sorter.visitIincInsn(2, 5);
        
        // Edge case: minimum var and increment values
        sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitIincInsn(int var, int increment) {
                Assert.assertEquals(0, var);
                Assert.assertEquals(Integer.MIN_VALUE, increment);
            }
        });
        sorter.visitIincInsn(0, Integer.MIN_VALUE);
    }
    
    @Test
    public void testVisitMaxs() {
        // Typical case
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                Assert.assertEquals(10, maxStack);
                Assert.assertEquals(20, maxLocals);
            }
        });
        sorter.visitMaxs(10, 20);
        
        // Edge case: minimum maxStack and maxLocals values
        sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                Assert.assertEquals(Integer.MIN_VALUE, maxStack);
                Assert.assertEquals(Integer.MIN_VALUE, maxLocals);
            }
        });
        sorter.visitMaxs(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
    
    @Test
    public void testVisitLocalVariable() {
        // Typical case
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                Assert.assertEquals("name", name);
                Assert.assertEquals("desc", desc);
                Assert.assertEquals("signature", signature);
                Assert.assertNotNull(start);
                Assert.assertNotNull(end);
                Assert.assertEquals(0, index);
            }
        });
        sorter.visitLocalVariable("name", "desc", "signature", new Label(), new Label(), 0);
        
        // Edge case: null name, desc and signature
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                Assert.assertNull(name);
                Assert.assertNull(desc);
                Assert.assertNull(signature);
                Assert.assertNotNull(start);
                Assert.assertNotNull(end);
                Assert.assertEquals(Integer.MIN_VALUE, index);
            }
        });
        sorter.visitLocalVariable(null, null, null, new Label(), new Label(), Integer.MIN_VALUE);
    }
    
    @Test
    public void testVisitFrame() {
        // Typical case
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                Assert.assertEquals(1, type);
                Assert.assertEquals(2, nLocal);
                Assert.assertNotNull(local);
                Assert.assertEquals(3, nStack);
                Assert.assertNotNull(stack);
            }
        });
        sorter.visitFrame(1, 2, new Object[]{}, 3, new Object[]{});
        
        // Edge case: type -1
        sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                Assert.assertEquals(-1, type);
                Assert.assertEquals(2, nLocal);
                Assert.assertNotNull(local);
                Assert.assertEquals(3, nStack);
                Assert.assertNotNull(stack);
            }
        });
        sorter.visitFrame(-1, 2, new Object[]{}, 3, new Object[]{});
    }
    
    @Test
    public void testNewLocal() {
        // Typical case
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {});
        int local = sorter.newLocal(Type.INT_TYPE);
        Assert.assertEquals(sorter.firstLocal, local);
        
        // Edge case: Type.Long
        sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {});
        local = sorter.newLocal(Type.LONG_TYPE);
        Assert.assertEquals(sorter.firstLocal, local);
        
        // Edge case: Type.FLOAT
        sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {});
        local = sorter.newLocal(Type.FLOAT_TYPE);
        Assert.assertEquals(sorter.firstLocal, local);
        
        // Edge case: Type.DOUBLE
        sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {});
        local = sorter.newLocal(Type.DOUBLE_TYPE);
        Assert.assertEquals(sorter.firstLocal, local);
        
        // Edge case: Type.OBJECT
        sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {});
        local = sorter.newLocal(Type.getObjectType("java/lang/Object"));
        Assert.assertEquals(sorter.firstLocal, local);
        
        // Edge case: Type.WHATEVER
        sorter = new LocalVariablesSorter(1, "desc", new MethodVisitor() {});
        local = sorter.newLocal(Type.getType("Lwhatever;"));
        Assert.assertEquals(sorter.firstLocal, local);
    }
    
    protected void setLocalType(int local, Type type) {
        // No-Op
    }
    
    private void setFrameLocal(int local, Object type) {
        // No-Op
    }
    
    private int remap(int var, Type type) {
        return var;
    }
    
    protected int newLocalMapping(Type type) {
        return 0;
    }
}