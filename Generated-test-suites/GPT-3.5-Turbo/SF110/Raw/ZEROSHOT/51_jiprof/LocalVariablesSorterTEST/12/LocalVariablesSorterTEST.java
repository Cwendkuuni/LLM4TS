import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    private MethodVisitor mv;
    private LocalVariablesSorter lvs;

    @Before
    public void setUp() {
        mv = mock(MethodVisitor.class);
        lvs = new LocalVariablesSorter(1, "test", mv);
    }

    @Test
    public void testVisitVarInsn() {
        lvs.visitVarInsn(21, 1);
        verify(mv).visitVarInsn(21, lvs.remap(1, Type.INT_TYPE));
    }

    @Test
    public void testVisitIincInsn() {
        lvs.visitIincInsn(2, 3);
        verify(mv).visitIincInsn(lvs.remap(2, Type.INT_TYPE), 3);
    }
    
    @Test
    public void testVisitMaxs() {
        lvs.visitMaxs(3, 4);
        verify(mv).visitMaxs(3, lvs.nextLocal);
    }
    
    @Test
    public void testVisitLocalVariable() {
        Label start = mock(Label.class);
        Label end = mock(Label.class);
        
        lvs.visitLocalVariable("name", "desc", "signature", start, end, 5);
        verify(mv).visitLocalVariable("name", "desc", "signature", start, end, lvs.remap(5, Type.getType("desc")));
    }
    
    @Test
    public void testVisitFrame1() {
        Object[] local = {1, 2, 3};
        Object[] stack = {4, 5, 6};
        lvs.changed = false;

        lvs.visitFrame(-1, local.length, local, stack.length, stack);
        verify(mv).visitFrame(-1, local.length, local, stack.length, stack);

        Object[] oldLocals = new Object[lvs.newLocals.length];
        System.arraycopy(lvs.newLocals, 0, oldLocals, 0, oldLocals.length);

        lvs.mv = mock(MethodVisitor.class);
        lvs.changed = true;

        lvs.visitFrame(-1, local.length, local, stack.length, stack);

        verify(lvs.mv).visitFrame(-1, local.length, lvs.newLocals, stack.length, stack);
        verify(lvs.mv, never()).visitFrame(anyInt(), anyInt(), any(Object[].class), anyInt(), any(Object[].class));

        lvs.newLocals = oldLocals;
    }
    
    @Test(expected = IllegalStateException.class)
    public void testVisitFrame2() {
        lvs.visitFrame(0, 0, null, 0, null);
    }
    
    @Test
    public void testNewLocal() {
        int local = 1;
        Type type = Type.INT_TYPE;
        
        int expectedLocal = lvs.nextLocal;
        lvs.nextLocal += type.getSize();

        int result = lvs.newLocal(type);
        
        verify(lvs).setLocalType(expectedLocal, type);
        verify(lvs).setFrameLocal(expectedLocal, local);
        
        assertEquals(expectedLocal, result);
    }

    @Test
    public void testSetLocalType() {
        int local = 1;
        Type type = Type.INT_TYPE;
        
        lvs.setLocalType(local, type);
        
        // No assertion, just verifying that the method is called without error
    }
    
    @Test
    public void testSetFrameLocal() {
        int local = 1;
        Object type = 2;
        
        lvs.newLocals = new Object[2];

        lvs.setFrameLocal(local, type);
        assertEquals(type, lvs.newLocals[local]);
    }

    @Test
    public void testRemap1() {
        int var = 1;
        Type type = Type.INT_TYPE;
        
        int result = lvs.remap(var, type);
        
        assertEquals(var, result);
    }

    @Test
    public void testRemap2() {
        int var = 2;
        Type type = Type.LONG_TYPE;
        
        int expectedLocal = 2 * var + type.getSize() - 1;
        int result = lvs.remap(var, type);
        
        assertEquals(lvs.mapping[expectedLocal], result + 1);
        assertTrue(lvs.changed);
    }
    
    @Test
    public void testNewLocalMapping() {
        int expectedLocal = lvs.nextLocal;
        lvs.nextLocal += Type.INT_TYPE.getSize();

        int result = lvs.newLocalMapping(Type.INT_TYPE);

        assertEquals(expectedLocal, result);
    }
}