
package org.objectweb.asm.jip.commons;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    @Mock
    MethodVisitor mv;

    LocalVariablesSorter localVariablesSorter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        localVariablesSorter = new LocalVariablesSorter(0, "desc", mv);
    }

    @Test
    public void visitVarInsnTest(){
        localVariablesSorter.visitVarInsn(22, 1);
        verify(mv).visitVarInsn(22, 1);
    }

    @Test
    public void visitIincInsnTest(){
        localVariablesSorter.visitIincInsn(5, 1);
        verify(mv).visitIincInsn(5, 1);
    }

    @Test
    public void visitMaxsTest(){
        localVariablesSorter.visitMaxs(10, 3);
        verify(mv).visitMaxs(10, 2);
    }

    @Test
    public void visitLocalVariableTest(){
        localVariablesSorter.visitLocalVariable("name", "desc", "signature", mock(Label.class), mock(Label.class), 2);
        verify(mv).visitLocalVariable("name", "desc", "signature", mock(Label.class), mock(Label.class), 2);
    }

    @Test
    public void visitFrameTest(){
        localVariablesSorter.visitFrame(0, 3, new Object[]{"a", 1, 2}, 5, new Object[]{"b", 3, 4});
        verify(mv).visitFrame(0, 3, new Object[]{"a", 1, 2}, 5, new Object[]{"b", 3, 4});
    }

    @Test
    public void newLocalTest(){
        Type type = Type.INT_TYPE;
        int local = localVariablesSorter.newLocal(type);
        Assert.assertEquals(1, local);
    }

    @Test
    public void setLocalTypeTest(){
    }

    @Test
    public void remapTest(){
        Type type = Type.INT_TYPE;
        int var = 1;
        int remappedVar = localVariablesSorter.remap(var, type);
        Assert.assertEquals(1, remappedVar);
    }

    @Test
    public void newLocalMappingTest(){
        Type type = Type.INT_TYPE;
        int mapping = localVariablesSorter.newLocalMapping(type);
        Assert.assertEquals(1, mapping);
    }

}

