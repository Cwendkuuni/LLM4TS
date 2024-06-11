package org.objectweb.asm.jip.commons;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.*;
import org.objectweb.asm.jip.MethodAdapter;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter localVariablesSorter;

    @Before
    public void setUp() {
        MethodVisitor mv = new MethodVisitor(null);
        localVariablesSorter = new LocalVariablesSorter(0, "", mv);
    }

    @Test
    public void testVarInsn() {
        // Testing visitVarInsn with opcode 22
        localVariablesSorter.visitVarInsn(22, 0);
        // Assert the remap method is called correctly
        assertEquals(0, localVariablesSorter.remap(0, Type.LONG_TYPE));

        // Testing visitVarInsn with opcode 24
        localVariablesSorter.visitVarInsn(24, 0);
        // Assert the remap method is called correctly
        assertEquals(0, localVariablesSorter.remap(0, Type.DOUBLE_TYPE));

        // Testing visitVarInsn with opcode 23
        localVariablesSorter.visitVarInsn(23, 0);
        // Assert the remap method is called correctly
        assertEquals(0, localVariablesSorter.remap(0, Type.FLOAT_TYPE));

        // Testing visitVarInsn with opcode 21
        localVariablesSorter.visitVarInsn(21, 0);
        // Assert the remap method is called correctly
        assertEquals(0, localVariablesSorter.remap(0, Type.INT_TYPE));

        // Testing visitVarInsn with default
        localVariablesSorter.visitVarInsn(25, 0);
        // Assert the remap method is called correctly
        assertEquals(0, localVariablesSorter.remap(0, LocalVariablesSorter.OBJECT_TYPE));
    }

    @Test
    public void testVisitIincInsn() {
        localVariablesSorter.visitIincInsn(0, 1);
        // Assert the remap method is called correctly
        assertEquals(0, localVariablesSorter.remap(0, Type.INT_TYPE));
    }

    @Test
    public void testVisitMaxs() {
        localVariablesSorter.visitMaxs(10, 5);
        // Assert the nextLocal value is set correctly
        assertEquals(5, localVariablesSorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        localVariablesSorter.visitLocalVariable("name", "desc", "signature", new Label(), new Label(), 0);
        // Assert the remap method is called correctly
        assertEquals(0, localVariablesSorter.remap(0, Type.getType("desc")));
    }

    @Test
    public void testVisitFrame() {
        Object[] local = new Object[]{"object"};
        Object[] stack = new Object[]{"object"};
        try {
            localVariablesSorter.visitFrame(0, 1, local, 1, stack);
            fail("Exception not thrown");
        } catch (IllegalStateException e) {
            assertEquals("ClassReader.accept() should be called with EXPAND_FRAMES flag", e.getMessage());
        }

        localVariablesSorter.changed = true;
        localVariablesSorter.visitFrame(0, 1, local, 1, stack);
        // Assert the setFrameLocal method is called correctly
        assertEquals(Object.class, localVariablesSorter.newLocals[0]);
        // Assert the mv.visitFrame method is called correctly
        assertEquals(1, localVariablesSorter.mv.nStack);

        localVariablesSorter.changed = false;
        localVariablesSorter.visitFrame(0, 1, local, 1, stack);
        // Assert the mv.visitFrame method is called correctly when changed is false
        assertEquals(1, localVariablesSorter.mv.nStack);
    }

    @Test
    public void testNewLocal() {
        // Testing sort 1
        localVariablesSorter.newLocal(Type.BYTE_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(1, localVariablesSorter.nextLocal);
        // Assert the method setFrameLocal is called correctly
        assertEquals(Opcodes.INTEGER, localVariablesSorter.newLocals[0]);

        // Testing sort 2
        localVariablesSorter.newLocal(Type.SHORT_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(2, localVariablesSorter.nextLocal);
        // Assert the method setFrameLocal is called correctly
        assertEquals(Opcodes.INTEGER, localVariablesSorter.newLocals[1]);

        // Testing sort 3
        localVariablesSorter.newLocal(Type.INT_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(3, localVariablesSorter.nextLocal);
        // Assert the method setFrameLocal is called correctly
        assertEquals(Opcodes.INTEGER, localVariablesSorter.newLocals[2]);

        // Testing sort 4
        localVariablesSorter.newLocal(Type.LONG_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(5, localVariablesSorter.nextLocal);
        // Assert the method setFrameLocal is called correctly
        assertEquals(Opcodes.LONG, localVariablesSorter.newLocals[3]);

        // Testing sort 5
        localVariablesSorter.newLocal(Type.FLOAT_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(6, localVariablesSorter.nextLocal);
        // Assert the method setFrameLocal is called correctly
        assertEquals(Opcodes.FLOAT, localVariablesSorter.newLocals[4]);

        // Testing sort 6
        localVariablesSorter.newLocal(Type.DOUBLE_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(8, localVariablesSorter.nextLocal);
        // Assert the method setFrameLocal is called correctly
        assertEquals(Opcodes.DOUBLE, localVariablesSorter.newLocals[6]);

        // Testing sort 7
        localVariablesSorter.newLocal(Type.getType("Ljava/lang/Integer;"));
        // Assert the nextLocal value is set correctly
        assertEquals(9, localVariablesSorter.nextLocal);
        // Assert the method setFrameLocal is called correctly
        assertEquals(Type.getType("Ljava/lang/Integer;"), localVariablesSorter.newLocals[8]);
    }

    @Test
    public void testRemap() {
        // Testing when var < firstLocal
        int result1 = localVariablesSorter.remap(0, Type.INT_TYPE);
        // Assert the var returned as is
        assertEquals(0, result1);

        localVariablesSorter.mapping[1] = 0;
        localVariablesSorter.nextLocal = 1;

        // Testing when var >= firstLocal and key >= mapping.length
        int result2 = localVariablesSorter.remap(2, Type.INT_TYPE);
        // Assert the value returned from newLocalMapping is correct
        assertEquals(1, result2);

        // Testing when var >= firstLocal and value is not 0
        localVariablesSorter.mapping[0] = 1;
        int result3 = localVariablesSorter.remap(1, Type.INT_TYPE);
        // Assert the value returned - 1 is correct
        assertEquals(0, result3);

        localVariablesSorter.setLocalType(0, Type.INT_TYPE);
        int result4 = localVariablesSorter.remap(1, Type.INT_TYPE);
        // Assert the changed value is set to true when value != var
        assertTrue(localVariablesSorter.changed);
        // Assert the value returned - 1 is correct
        assertEquals(0, result4);
    }

    @Test
    public void testNewLocalMapping() {
        // Testing sort 1
        int result1 = localVariablesSorter.newLocalMapping(Type.BYTE_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(1, localVariablesSorter.nextLocal);
        // Assert the returned value is correct
        assertEquals(0, result1);

        // Testing sort 2
        int result2 = localVariablesSorter.newLocalMapping(Type.SHORT_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(2, localVariablesSorter.nextLocal);
        // Assert the returned value is correct
        assertEquals(1, result2);

        // Testing sort 3
        int result3 = localVariablesSorter.newLocalMapping(Type.INT_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(3, localVariablesSorter.nextLocal);
        // Assert the returned value is correct
        assertEquals(2, result3);

        // Testing sort 4
        int result4 = localVariablesSorter.newLocalMapping(Type.LONG_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(5, localVariablesSorter.nextLocal);
        // Assert the returned value is correct
        assertEquals(3, result4);

        // Testing sort 5
        int result5 = localVariablesSorter.newLocalMapping(Type.FLOAT_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(6, localVariablesSorter.nextLocal);
        // Assert the returned value is correct
        assertEquals(4, result5);

        // Testing sort 6
        int result6 = localVariablesSorter.newLocalMapping(Type.DOUBLE_TYPE);
        // Assert the nextLocal value is set correctly
        assertEquals(8, localVariablesSorter.nextLocal);
        // Assert the returned value is correct
        assertEquals(6, result6);

        // Testing sort 7
        int result7 = localVariablesSorter.newLocalMapping(Type.getType("Ljava/lang/Integer;"));
        // Assert the nextLocal value is set correctly
        assertEquals(9, localVariablesSorter.nextLocal);
        // Assert the returned value is correct
        assertEquals(8, result7);
    }

}