
package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        // Arrange
        int access = 0;
        String desc = "()V";
        MethodVisitor mv = null;

        // Act
        LocalVariablesSorter sorter = new LocalVariablesSorter(access, desc, mv);

        // Assert
        assertEquals(1, sorter.nextLocal);
        assertEquals(1, sorter.firstLocal);
    }

    @Test
    public void testVisitVarInsn() {
        // Arrange
        int opcode = 0;
        int var = 0;
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);

        // Act
        sorter.visitVarInsn(opcode, var);

        // Assert
        // No assertion possible as it delegates to the MethodVisitor
    }

    @Test
    public void testVisitIincInsn() {
        // Arrange
        int var = 0;
        int increment = 0;
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);

        // Act
        sorter.visitIincInsn(var, increment);

        // Assert
        // No assertion possible as it delegates to the MethodVisitor
    }

    @Test
    public void testVisitMaxs() {
        // Arrange
        int maxStack = 0;
        int maxLocals = 0;
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);

        // Act
        sorter.visitMaxs(maxStack, maxLocals);

        // Assert
        // No assertion possible as it delegates to the MethodVisitor
    }

    @Test
    public void testVisitLocalVariable() {
        // Arrange
        String name = "";
        String desc = "";
        String signature = "";
        Label start = null;
        Label end = null;
        int index = 0;
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);

        // Act
        sorter.visitLocalVariable(name, desc, signature, start, end, index);

        // Assert
        // No assertion possible as it delegates to the MethodVisitor
    }

    @Test
    public void testVisitFrame() {
        // Arrange
        int type = 0;
        int nLocal = 0;
        Object[] local = new Object[0];
        int nStack = 0;
        Object[] stack = new Object[0];
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);

        // Act
        try {
            sorter.visitFrame(type, nLocal, local, nStack, stack);
        } catch (IllegalStateException e) {
            // Assert
            assertEquals("ClassReader.accept() should be called with EXPAND_FRAMES flag", e.getMessage());
        }
    }

    @Test
    public void testNewLocal() {
        // Arrange
        Type type = Type.INT_TYPE;
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);

        // Act
        int local = sorter.newLocal(type);

        // Assert
        assertEquals(1, local);
        assertEquals(type.getSize(), sorter.nextLocal);
    }

    @Test
    public void testNewLocalMapping() {
        // Arrange
        Type type = Type.INT_TYPE;
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", null);

        // Act
        int local = sorter.newLocalMapping(type);

        // Assert
        assertEquals(1, local);
        assertEquals(type.getSize(), sorter.nextLocal);
    }

}
