
import org.junit.Test;
import org.objectweb.asm.*;
import org.objectweb.asm.jip.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        int access = 1;
        String desc = "(I)V";
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(access, desc, mv);

        assertEquals(0, sorter.mapping.length);
        assertEquals(0, sorter.newLocals.length);
        assertEquals(1, sorter.nextLocal);
        assertEquals(1, sorter.firstLocal);
        assertEquals(false, sorter.changed);

        verify(mv, times(1)).visitVarInsn(eq(Opcodes.ALOAD), eq(0)); // visitVarInsn method should be called once
    }

    @Test
    public void testVisitVarInsn() {
        int opcode = Opcodes.ALOAD;
        int var = 1;
        Type type = LocalVariablesSorter.OBJECT_TYPE;
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", mv);
        sorter.visitVarInsn(opcode, var);

        verify(mv, times(1)).visitVarInsn(eq(opcode), eq(sorter.remap(var, type)));
    }

    @Test
    public void testVisitIincInsn() {
        int var = 1;
        int increment = 10;
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", mv);
        sorter.visitIincInsn(var, increment);

        verify(mv, times(1))
            .visitIincInsn(eq(sorter.remap(var, Type.INT_TYPE)), eq(increment));
    }

    @Test
    public void testVisitMaxs() {
        int maxStack = 10;
        int maxLocals = 20;
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", mv);
        sorter.visitMaxs(maxStack, maxLocals);

        verify(mv, times(1))
            .visitMaxs(eq(maxStack), eq(sorter.nextLocal));
    }

    @Test
    public void testVisitLocalVariable() {
        String name = "var";
        String desc = "I";
        String signature = "";
        Label start = new Label();
        Label end = new Label();
        int index = 1;
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", mv);
        sorter.visitLocalVariable(name, desc, signature, start, end, index);

        verify(mv, times(1))
            .visitLocalVariable(eq(name), eq(desc), eq(signature), eq(start), eq(end), eq(sorter.remap(index, Type.getType(desc))));
    }

    @Test
    public void testVisitFrame_whenTypeNotEqualsMinusOne() {
        int type = 1;
        int nLocal = 10;
        Object[] local = new Object[nLocal];
        int nStack = 5;
        Object[] stack = new Object[nStack];
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", mv);
        sorter.visitFrame(type, nLocal, local, nStack, stack);

        verify(mv, times(1))
            .visitFrame(eq(type), eq(nLocal), eq(local), eq(nStack), eq(stack));
    }

    @Test(expected = IllegalStateException.class)
    public void testVisitFrame_whenTypeEqualsMinusOne_and_changedIsFalse() {
        int type = -1;
        int nLocal = 10;
        Object[] local = new Object[nLocal];
        int nStack = 5;
        Object[] stack = new Object[nStack];
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", mv);
        sorter.visitFrame(type, nLocal, local, nStack, stack);
    }

    @Test
    public void testVisitFrame_whenTypeEqualsMinusOne_and_changedIsTrue() {
        int type = -1;
        int nLocal = 10;
        Object[] local = new Object[nLocal];
        int nStack = 5;
        Object[] stack = new Object[nStack];
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", mv);
        sorter.changed = true;
        sorter.visitFrame(type, nLocal, local, nStack, stack);

        verify(mv, times(1))
            .visitFrame(eq(type), eq(sorter.nextLocal), eq(sorter.newLocals), eq(nStack), eq(stack));
        assertEquals(sorter.newLocals.length, sorter.newLocals.length);
    }

    @Test
    public void testNewLocal() {
        Type type = Type.INT_TYPE;
        MethodVisitor mv = mock(MethodVisitor.class);

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", mv);
        int local = sorter.newLocal(type);

        verify(mv, never())
            .visitVarInsn(eq(Opcodes.ALOAD), anyInt()); // visitVarInsn method should not be called

        assertEquals(sorter.nextLocal - type.getSize(), local);
        assertEquals(type.getSize(), sorter.nextLocal - local);
    }

    @Test
    public void testSetLocalType() {
        int local = 1;
        Type type = Type.INT_TYPE;

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", null);
        sorter.setLocalType(local, type);
    }

    @Test
    public void testSetFrameLocal() {
        int local = 1;
        Object type = Type.INT_TYPE;

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", null);
        sorter.setFrameLocal(local, type);
    }

    @Test
    public void testRemap_whenVarLessThanFirstLocal() {
        int var = 1;
        Type type = Type.INT_TYPE;

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", null);
        int remappedVar = sorter.remap(var, type);

        assertEquals(var, remappedVar);
        assertEquals(false, sorter.changed);
    }

    @Test
    public void testRemap_whenVarGreaterThanEqualThanFirstLocal() {
        int var = 2;
        Type type = Type.INT_TYPE;

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", null);
        int remappedVar = sorter.remap(var, type);

        assertEquals(1, remappedVar);
        assertEquals(true, sorter.changed);
    }

    @Test
    public void testNewLocalMapping() {
        Type type = Type.INT_TYPE;

        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "(I)V", null);
        int local = sorter.newLocalMapping(type);

        assertEquals(sorter.nextLocal - type.getSize(), local);
        assertEquals(type.getSize(), sorter.nextLocal - local);
    }
}
