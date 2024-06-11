package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class LocalVariablesSorterTest {
	@Test
	public void testConstructor() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		assertEquals(1, lvs.nextLocal);
		assertEquals(1, lvs.firstLocal);
	}
	
	@Test
	public void testVisitVarInsn_IntegerType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		lvs.visitVarInsn(Opcodes.ILOAD, 1);
		verify(mv).visitVarInsn(Opcodes.ILOAD, 1);
	}
	
	@Test
	public void testVisitVarInsn_LongType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		lvs.visitVarInsn(Opcodes.LLOAD, 1);
		verify(mv).visitVarInsn(Opcodes.LLOAD, 2);
	}
	
	@Test
	public void testVisitVarInsn_FloatType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		lvs.visitVarInsn(Opcodes.FLOAD, 1);
		verify(mv).visitVarInsn(Opcodes.FLOAD, 1);
	}
	
	@Test
	public void testVisitVarInsn_DoubleType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		lvs.visitVarInsn(Opcodes.DLOAD, 1);
		verify(mv).visitVarInsn(Opcodes.DLOAD, 3);
	}
	
	@Test
	public void testVisitVarInsn_ObjectType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		lvs.visitVarInsn(Opcodes.ALOAD, 1);
		verify(mv).visitVarInsn(Opcodes.ALOAD, 1);
	}
	
	@Test
	public void testVisitIincInsn() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		lvs.visitIincInsn(1, 1);
		verify(mv).visitIincInsn(1, 1);
	}
	
	@Test
	public void testVisitMaxs() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		lvs.visitMaxs(10, 20);
		verify(mv).visitMaxs(10, 1);
	}
	
	@Test
	public void testVisitLocalVariable() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		lvs.visitLocalVariable("name", "desc", "signature", null, null, 1);
		verify(mv).visitLocalVariable("name", "desc", "signature", null, null, 1);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testVisitFrame_IllegalStateException() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		lvs.visitFrame(1, 2, new Object[2], 3, new Object[3]);
	}
	
	@Test
	public void testVisitFrame() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		lvs.changed = true;
		lvs.newLocals = new Object[10];
		
		Object[] locals = {
				Opcodes.INTEGER,
				Opcodes.FLOAT,
				Opcodes.LONG,
				null
		};
		Object[] expectedLocals = {
				Opcodes.INTEGER,
				Opcodes.FLOAT,
				Opcodes.LONG,
				Opcodes.TOP,
				Opcodes.TOP,
				null,
				null,
				null
		};
		
		lvs.visitFrame(-1, 4, locals, 0, null);
		
		verify(mv).visitFrame(-1, 5, expectedLocals, 0, null);
	}
	
	@Test
	public void testNewLocal_IntegerType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		int local = lvs.newLocal(Type.INT_TYPE);
		
		assertEquals(1, local);
		assertEquals(2, lvs.nextLocal);
	}
	
	@Test
	public void testNewLocal_FloatType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		int local = lvs.newLocal(Type.FLOAT_TYPE);
		
		assertEquals(1, local);
		assertEquals(2, lvs.nextLocal);
	}
	
	@Test
	public void testNewLocal_LongType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		int local = lvs.newLocal(Type.LONG_TYPE);
		
		assertEquals(1, local);
		assertEquals(3, lvs.nextLocal);
	}
	
	@Test
	public void testNewLocal_DoubleType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		int local = lvs.newLocal(Type.DOUBLE_TYPE);
		
		assertEquals(1, local);
		assertEquals(3, lvs.nextLocal);
	}
	
	@Test
	public void testNewLocal_ObjectType() {
		MethodVisitor mv = mock(MethodVisitor.class);
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", mv);
		
		int local = lvs.newLocal(Type.getObjectType("java/lang/Object"));
		
		assertEquals(1, local);
		assertEquals(1, lvs.nextLocal);
	}
	
	@Test
	public void testSetLocalType() {
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", null);
		
		lvs.setLocalType(1, Type.INT_TYPE);
	}
	
	@Test
	public void testSetFrameLocal_ExistingLocal() {
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", null);
		lvs.newLocals = new Object[10];
		
		lvs.setFrameLocal(1, Opcodes.INTEGER);
		verifyExistingLocal(lvs.newLocals, 1, Opcodes.INTEGER);
	}
	
	@Test
	public void testSetFrameLocal_NewLocal() {
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", null);
		lvs.newLocals = new Object[2];
		
		lvs.setFrameLocal(2, Opcodes.INTEGER);
		verifyNewLocal(lvs.newLocals, 2, Opcodes.INTEGER);
	}
	
	@Test
	public void testRemap_VarLessThanFirstLocal() {
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", null);
		lvs.firstLocal = 5;
		
		int remapped = lvs.remap(3, Type.INT_TYPE);
		
		assertEquals(3, remapped);
	}
	
	@Test
	public void testRemap_VarGreaterThanFirstLocal() {
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", null);
		lvs.firstLocal = 2;
		lvs.newLocals = new Object[4];
		
		int remapped = lvs.remap(3, Type.INT_TYPE);
		
		assertEquals(1, remapped);
		assertEquals(0, lvs.changed ? 1 : 0);
	}
	
	@Test
	public void testRemap_VarEqualToFirstLocal() {
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", null);
		lvs.firstLocal = 1;
		lvs.newLocals = new Object[4];
		
		int remapped = lvs.remap(1, Type.INT_TYPE);
		
		assertEquals(1, remapped);
		assertEquals(0, lvs.changed ? 1 : 0);
	}
	
	@Test
	public void testNewLocalMapping() {
		LocalVariablesSorter lvs = new LocalVariablesSorter(0, "()V", null);
		
		int local = lvs.newLocalMapping(Type.INT_TYPE);
		
		assertEquals(1, local);
		assertEquals(2, lvs.nextLocal);
	}
	
	private void verifyExistingLocal(Object[] locals, int index, Object value) {
		assertEquals(value, locals[index]);
	}
	
	private void verifyNewLocal(Object[] locals, int index, Object value) {
		assertEquals(value, locals[index]);
	}
}