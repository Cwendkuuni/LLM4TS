import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.MethodVisitor;

public class LocalVariablesSorterTest {
    
    @Test
    public void testLocalVariablesSorterConstructor() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);
        assertNotNull(sorter);
    }

    @Test
    public void testVisitVarInsn() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);
        
        sorter.visitVarInsn(21, 1);
        
        verify(mv).visitVarInsn(21, 1);
    }

    @Test
    public void testVisitMaxs() {
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", mv);
        
        sorter.visitMaxs(5, 10);
        
        verify(mv).visitMaxs(5, 1);
    }

}