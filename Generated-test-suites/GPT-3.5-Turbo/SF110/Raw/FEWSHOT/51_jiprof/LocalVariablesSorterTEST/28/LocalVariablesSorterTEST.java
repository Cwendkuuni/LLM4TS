import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertNull;

public class LocalVariablesSorterTest {
    @Test
    public void testVisitVarInsn() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        sorter.visitVarInsn(22, 1);
        verify(mv).visitVarInsn(22, sorter.remap(1, Type.LONG_TYPE));
        
        sorter.visitVarInsn(24, 2);
        verify(mv).visitVarInsn(24, sorter.remap(2, Type.DOUBLE_TYPE));
        
        sorter.visitVarInsn(23, 3);
        verify(mv).visitVarInsn(23, sorter.remap(3, Type.FLOAT_TYPE));
        
        sorter.visitVarInsn(21, 4);
        verify(mv).visitVarInsn(21, sorter.remap(4, Type.INT_TYPE));
        
        sorter.visitVarInsn(25, 5);
        verify(mv).visitVarInsn(25, sorter.remap(5, LocalVariablesSorter.OBJECT_TYPE));
    }
    
    @Test
    public void testVisitIincInsn() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        sorter.visitIincInsn(1, 10);
        verify(mv).visitIincInsn(sorter.remap(1, Type.INT_TYPE), 10);
    }
    
    @Test
    public void testVisitMaxs() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        sorter.visitMaxs(10, 20);
        verify(mv).visitMaxs(10, sorter.nextLocal);
    }
    
    @Test
    public void testVisitLocalVariable() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        sorter.visitLocalVariable("name", "desc", "signature", new Label(), new Label(), 1);
        verify(mv).visitLocalVariable("name", "desc", "signature", new Label(), new Label(), sorter.remap(1, Type.getType("desc")));
    }
    
    @Test
    public void testVisitFrame() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        sorter.changed = true;
        
        Object[] local = new Object[5];
        Object[] stack = new Object[5];
        
        sorter.visitFrame(-1, 5, local, 5, stack);
        verify(mv).visitFrame(-1, 5, sorter.newLocals, 5, stack);
    }
    
    @Test
    public void testNewLocal() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        Type type = Type.INT_TYPE;
        
        int result = sorter.newLocal(type);
        
        assertNull(sorter.newLocals[result]);
        verify(mv).visitVarInsn(100, result);
        assertEquals(sorter.remap(result, type), result);
    }
    
    @Test
    public void testSetLocalType() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        Type type = Type.INT_TYPE;
        
        sorter.setLocalType(1, type);
        assertNull(sorter.newLocals[1]);
    }
    
    @Test
    public void testSetFrameLocal() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        int local = 1;
        Object type = Type.INT_TYPE;
        
        sorter.setFrameLocal(local, type);
        assertEquals(type, sorter.newLocals[local]);
    }
    
    @Test
    public void testRemap() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        int var = 1;
        Type type = Type.DOUBLE_TYPE;
        
        int result = sorter.remap(var, type);
        
        assertEquals(result, 1);
        verify(mv).visitVarInsn(1, result);
        assertTrue(sorter.changed);
    }
    
    @Test
    public void testNewLocalMapping() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(1, "desc", mv);
        
        Type type = Type.LONG_TYPE;
        
        int result = sorter.newLocalMapping(type);
        
        assertTrue(result >= 1);
        assertTrue(result < sorter.nextLocal);
        assertEquals(sorter.nextLocal, result + type.getSize());
    }
}