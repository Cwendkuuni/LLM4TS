package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodAdapter;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {
  
  private LocalVariablesSorter localVariablesSorter;
  private MethodVisitor mv;

  @Before
  public void setUp() {
    mv = mock(MethodVisitor.class);
    localVariablesSorter = new LocalVariablesSorter(1, "desc", mv);
  }

  @Test
  public void testVisitVarInsn() {
    localVariablesSorter.visitVarInsn(22, 1);
    verify(mv).visitVarInsn(22, 1);
    localVariablesSorter.visitVarInsn(55, 1);
    verify(mv).visitVarInsn(55, 1);
    localVariablesSorter.visitVarInsn(23, 1);
    verify(mv).visitVarInsn(23, 1);
    localVariablesSorter.visitVarInsn(56, 1);
    verify(mv).visitVarInsn(56, 1);
    localVariablesSorter.visitVarInsn(21, 1);
    verify(mv).visitVarInsn(21, 1);
    localVariablesSorter.visitVarInsn(54, 1);
    verify(mv).visitVarInsn(54, 1);
    localVariablesSorter.visitVarInsn(77, 1);
    verify(mv).visitVarInsn(77, 1);
  }

  @Test
  public void testVisitIincInsn() {
    localVariablesSorter.visitIincInsn(1, 2);
    verify(mv).visitIincInsn(1, 2);
  }

  @Test
  public void testVisitMaxs() {
    localVariablesSorter.visitMaxs(1, 2);
    verify(mv).visitMaxs(1, 1);
  }

  @Test
  public void testVisitLocalVariable() {
    Label start = new Label();
    Label end = new Label();
    localVariablesSorter.visitLocalVariable("name", "desc", "signature", start, end, 1);
    verify(mv).visitLocalVariable("name", "desc", "signature", start, end, 1);
  }

  @Test
  public void testVisitFrame() {
    Object[] local = new Object[1];
    Object[] stack = new Object[1];
    localVariablesSorter.visitFrame(1, 1, local, 1, stack);
    verify(mv).visitFrame(1, 1, local, 1, stack);
  }

  @Test(expected = IllegalStateException.class)
  public void testVisitFrameWithIllegalStateException() {
    localVariablesSorter.visitFrame(-1, 1, new Object[1], 1, new Object[1]);
  }

  @Test
  public void testNewLocal() {
    int local = localVariablesSorter.newLocal(Type.INT_TYPE);
    assertEquals(1, local);
  }

  @Test
  public void testSetLocalType() {
    localVariablesSorter.setLocalType(1, Type.INT_TYPE);
    // Verify setLocalType called
  }

  @Test
  public void testSetFrameLocal() {
    localVariablesSorter.setFrameLocal(1, Opcodes.INTEGER);
    // Verify setFrameLocal called
  }

  @Test
  public void testRemap() {
    int remappedVar = localVariablesSorter.remap(1, Type.INT_TYPE);
    assertEquals(1, remappedVar);
  }

  @Test
  public void testNewLocalMapping() {
    int localMapping = localVariablesSorter.newLocalMapping(Type.INT_TYPE);
    assertEquals(1, localMapping);
  }

}