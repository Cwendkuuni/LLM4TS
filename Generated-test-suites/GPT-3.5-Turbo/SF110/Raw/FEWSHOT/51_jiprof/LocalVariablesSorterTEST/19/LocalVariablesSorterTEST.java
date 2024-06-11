package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {
    
    private class MockMethodVisitor extends MethodVisitor {
        public MockMethodVisitor(MethodVisitor mv) {
            super(mv);
        }

        @Override
        public void visitVarInsn(int opcode, int var) {
            
        }
        
        @Override
        public void visitIincInsn(int var, int increment) {
            
        }
        
        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            
        }
        
        @Override
        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            
        }
        
        @Override
        public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
            
        }
    }
    
    @Test
    public void testNewLocal() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MockMethodVisitor(null));
        
        assertEquals(1, sorter.newLocal(Type.INT_TYPE));
        assertEquals(2, sorter.newLocal(Type.FLOAT_TYPE));
        assertEquals(4, sorter.newLocal(Type.LONG_TYPE));
        assertEquals(6, sorter.newLocal(Type.DOUBLE_TYPE));
        assertEquals(8, sorter.newLocal(Type.getType("Ljava/lang/Object;")));
    }
    
    @Test
    public void testSetLocalType() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MockMethodVisitor(null));
        
        sorter.setLocalType(1, Type.INT_TYPE);
        sorter.setLocalType(2, Type.FLOAT_TYPE);
        sorter.setLocalType(3, Type.LONG_TYPE);
        sorter.setLocalType(4, Type.DOUBLE_TYPE);
        sorter.setLocalType(5, Type.getType("Ljava/lang/Object;"));
    }
    
    @Test
    public void testRemap() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MockMethodVisitor(null));
        
        assertEquals(1, sorter.remap(1, Type.INT_TYPE));
        assertEquals(2, sorter.remap(2, Type.FLOAT_TYPE));
        assertEquals(3, sorter.remap(3, Type.LONG_TYPE));
        assertEquals(4, sorter.remap(4, Type.DOUBLE_TYPE));
        assertEquals(5, sorter.remap(5, Type.getType("Ljava/lang/Object;")));
    }
    
    @Test
    public void testNewLocalMapping() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MockMethodVisitor(null));
        
        assertEquals(1, sorter.newLocalMapping(Type.INT_TYPE));
        assertEquals(2, sorter.newLocalMapping(Type.FLOAT_TYPE));
        assertEquals(3, sorter.newLocalMapping(Type.LONG_TYPE));
        assertEquals(4, sorter.newLocalMapping(Type.DOUBLE_TYPE));
        assertEquals(5, sorter.newLocalMapping(Type.getType("Ljava/lang/Object;")));
    }
}