package org.objectweb.asm.jip.commons;

import static org.junit.Assert.*;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodAdapter;


public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        // Test case for constructor with access, desc, and mv parameters
        MethodVisitor mv = new MethodVisitor(0) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                // Test case for visitVarInsn method
            	Label start = new Label();
                Label end = new Label();
                visitLocalVariable("name", "desc", "signature", start, end, var);
            }
            
            @Override
            public void visitIincInsn(int var, int increment) {
                // Test case for visitIincInsn method
            	assertEquals(2, var);
            	assertEquals(10, increment);
            }
            
            @Override
            public void visitMaxs(int maxStack, int maxLocals) {
                // Test case for visitMaxs method
            	assertEquals(10, maxStack);
            	assertEquals(5, maxLocals);
            }
            
            @Override
            public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                // Test case for visitLocalVariable method
            	assertEquals("name", name);
            	assertEquals("desc", desc);
            	assertEquals("signature", signature);
            	assertEquals(0, index);
            }
            
            @Override
            public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
                // Test case for visitFrame method
            	assertEquals(1, type);
            	assertEquals(2, nLocal);
            	assertEquals(3, nStack);
            }
        };
        
        LocalVariablesSorter lvs = new LocalVariablesSorter(1, "desc", mv);
        
        // Test case for newLocal method
        assertEquals(0, lvs.newLocal(Type.INT_TYPE));
        
        // Test case for setLocalType method
        lvs.setLocalType(0, Type.INT_TYPE);
        
        // Test case for newLocalMapping method
        assertEquals(5, lvs.newLocalMapping(Type.INT_TYPE));
    }
}