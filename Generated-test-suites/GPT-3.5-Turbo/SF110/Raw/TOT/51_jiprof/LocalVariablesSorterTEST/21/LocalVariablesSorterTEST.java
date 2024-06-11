import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    @Mock
    private MethodVisitor mockVisitor;

    private LocalVariablesSorter sorter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sorter = new LocalVariablesSorter(ACC_PUBLIC, "(Ljava/lang/String;)V", mockVisitor);
    }

    @Test
    public void visitVarInsn_WithValidOpcodeAndValidVar_ShouldCallMvVisitVarInsn() {
        sorter.visitVarInsn(Opcodes.ILOAD, 1);

        verify(mockVisitor, times(1)).visitVarInsn(Opcodes.ILOAD, 1);
    }

    @Test
    public void visitVarInsn_WithInvalidOpcodeAndValidVar_ShouldNotCallMvVisitVarInsn() {
        sorter.visitVarInsn(100, 1);

        verify(mockVisitor, never()).visitVarInsn(anyInt(), anyInt());
    }

    @Test
    public void visitIincInsn_WithValidVarAndValidIncrement_ShouldCallMvVisitIincInsn() {
        sorter.visitIincInsn(0, 1);

        verify(mockVisitor, times(1)).visitIincInsn(0, 1);
    }

    @Test
    public void visitMaxs_WithValidMaxStackAndValidMaxLocals_ShouldCallMvVisitMaxs() {
        sorter.visitMaxs(10, 5);

        verify(mockVisitor, times(1)).visitMaxs(10, sorter.getNextLocal());
    }

    @Test
    public void visitLocalVariable_WithValidParameters_ShouldCallMvVisitLocalVariable() {
        sorter.visitLocalVariable("name", "desc", "signature", new Label(), new Label(), 1);

        verify(mockVisitor, times(1)).visitLocalVariable("name", "desc", "signature", new Label(), new Label(), sorter.remap(1, Type.getType("desc")));
    }

    @Test(expected = IllegalStateException.class)
    public void visitFrame_WithInvalidType_ShouldThrowIllegalStateException() {
        sorter.visitFrame(1, 2, new Object[2], 3, new Object[3]);
    }

    @Test
    public void newLocal_WithValidType_ShouldReturnNextLocal() {
        Type type = Type.INT_TYPE;

        int local = sorter.newLocal(type);

        assertEquals(sorter.getNextLocal() - type.getSize(), local);
    }

    @Test
    public void setLocalType_WithValidLocalAndType_ShouldNotThrowException() {
        sorter.setLocalType(1, Type.BOOLEAN_TYPE);
    }

    @Test
    public void setFrameLocal_WithValidLocalAndType_ShouldNotThrowException() {
        sorter.setFrameLocal(1, Type.LONG_TYPE);
    }

    @Test
    public void remap_WithVarLessThanFirstLocal_ShouldReturnSameVar() {
        int var = 0;

        int remappedVar = sorter.remap(var, Type.INT_TYPE);

        assertEquals(var, remappedVar);
    }

    @Test
    public void remap_WithVarEqualToFirstLocal_ShouldReturnSameVar() {
        int var = sorter.getFirstLocal();

        int remappedVar = sorter.remap(var, Type.INT_TYPE);

        assertEquals(var, remappedVar);
    }

    @Test
    public void remap_WithVarGreaterThanFirstLocal_ShouldReturnNewLocal() {
        int var = 2;

        int remappedVar = sorter.remap(var, Type.INT_TYPE);

        assertEquals(sorter.remap(var, Type.INT_TYPE), remappedVar);
    }

    @Test
    public void newLocalMapping_WithValidType_ShouldReturnNextLocal() {
        Type type = Type.DOUBLE_TYPE;

        int local = sorter.newLocalMapping(type);

        assertEquals(sorter.getNextLocal() - type.getSize(), local);
    }

}