import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodAdapter;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {

    private LocalVariablesSorter sorter;
    
    @Before
    public void setUp(){
        MethodVisitor mv = new MethodAdapter();
        sorter = new LocalVariablesSorter(Opcodes.ACC_PUBLIC, "()V", mv);
    }
    
    @Test
    public void testVisitVarInsn(){
        // Test visitVarInsn
        sorter.visitVarInsn(Opcodes.ALOAD, 0);
        assertEquals(1, sorter.nextLocal);
    }
    
    @Test
    public void testVisitIincInsn(){
        // Test visitIincInsn
        sorter.visitIincInsn(1, 1);
        assertEquals(1, sorter.nextLocal);
    }
    
    @Test
    public void testVisitMaxs(){
        // Test visitMaxs
        sorter.visitMaxs(5, 10);
        assertEquals(10, sorter.nextLocal);
    }
    
    @Test
    public void testVisitLocalVariable(){
        // Test visitLocalVariable
        Label start = new Label();
        Label end = new Label();
        sorter.visitLocalVariable("variable", "I", null, start, end, 0);
        assertEquals(1, sorter.nextLocal);
    }
    
    @Test
    public void testVisitFrame(){
        // Test visitFrame
        Object[] local = new Object[]{"I"};
        Object[] stack = new Object[]{};
        sorter.visitFrame(Opcodes.F_NEW, 1, local, 0, stack);
        assertEquals(1, sorter.nextLocal);
    }
    
    @Test
    public void testNewLocal(){
        // Test newLocal
        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        assertEquals(1, sorter.nextLocal);
        assertEquals(type.getDescriptor(), sorter.newLocals[local]);
    }
    
    @Test
    public void testSetLocalType(){
        // Test setLocalType
        Type type = Type.INT_TYPE;
        int local = sorter.newLocal(type);
        sorter.setLocalType(local, type);
        assertEquals(type.getDescriptor(), sorter.newLocals[local]);
    }
    
    @Test
    public void testSetFrameLocal(){
        // Test setFrameLocal
        int local = 0;
        Object type = Opcodes.INTEGER;
        sorter.setFrameLocal(local, type);
        assertEquals(type, sorter.newLocals[local]);
    }
    
    @Test
    public void testRemap(){
        // Test remap
        int var = 0;
        Type type = Type.INT_TYPE;
        int result = sorter.remap(var, type);
        assertEquals(0, result);
        assertEquals(var+1, sorter.mapping[2*var+type.getSize()-1]);
        assertFalse(sorter.changed);
    }
    
    @Test
    public void testNewLocalMapping(){
        // Test newLocalMapping
        Type type = Type.INT_TYPE;
        int result = sorter.newLocalMapping(type);
        assertEquals(1, sorter.nextLocal);
        assertEquals(0, result);
    }
}