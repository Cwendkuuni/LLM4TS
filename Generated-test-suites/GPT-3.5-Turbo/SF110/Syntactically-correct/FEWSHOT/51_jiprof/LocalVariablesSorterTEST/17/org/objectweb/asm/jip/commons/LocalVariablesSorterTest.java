package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodAdapter;

public class LocalVariablesSorterTest {
	
	private MethodVisitor mv;
	private LocalVariablesSorter sorter;
	private Type type;
	
	@Before
	public void setUp() {
		mv = mock(MethodVisitor.class);
		sorter = new LocalVariablesSorter(10, "desc", mv);
		type = mock(Type.class);
	}
	
	@Test
	public void testConstructor() {
		assertNotNull(sorter.mapping);
		assertNotNull(sorter.newLocals);
		assertEquals(40, sorter.mapping.length);
		assertEquals(20, sorter.newLocals.length);
		assertEquals(1, sorter.nextLocal);
		assertEquals(1, sorter.firstLocal);
	}
	
	@Test
	public void testVarInsn() {
		sorter.visitVarInsn(22, 5);
		verify(mv, times(1)).visitVarInsn(22, 0);
	}
	
	@Test
	public void testVisitIincInsn() {
		sorter.visitIincInsn(5, 5);
		verify(mv, times(1)).visitIincInsn(0, 5);
	}
	
	@Test
	public void testVisitMaxs() {
		sorter.visitMaxs(0, 0);
		verify(mv, times(1)).visitMaxs(0, 1);
	}
	
	@Test
	public void testVisitLocalVariable() {
		Label start = new Label();
		Label end = new Label();
		
		sorter.visitLocalVariable("name", "desc", "signature", start, end, 0);
		verify(mv, times(1)).visitLocalVariable("name", "desc", "signature", start, end, 0);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testVisitFrame() {
		Object[] local = new Object[1];
		Object[] stack = new Object[1];
		
		sorter.visitFrame(-1, 0, local, 0, stack);
		verify(mv, times(1)).visitFrame(-1, 1, local, 0, stack);
	}
	
	@Test
	public void testNewLocal() {
		when(type.getSort()).thenReturn(1);
		when(type.getSize()).thenReturn(2);
		when(type.getDescriptor()).thenReturn("descriptor");
		when(type.getInternalName()).thenReturn("internalName");
		
		int local = sorter.newLocal(type);
		
		assertNotNull(local);
		assertEquals(1, sorter.nextLocal);
		assertEquals(2, sorter.newLocals.length);
		assertEquals(2, sorter.newLocals[local]);
	}
	
	@Test
	public void testSetLocalType() {
		sorter.setLocalType(0, type);
	}
	
	@Test
	public void testSetFrameLocal() {
		sorter.setFrameLocal(0, type);
	}
	
	@Test
	public void testRemap() {
		int var = 0;
		Type type = Type.getType("type");
		int remapped = sorter.remap(var, type);
		
		assertEquals(var, remapped);
		assertEquals(0, sorter.mapping.length);
		assertEquals(1, sorter.nextLocal);
		assertEquals(1, sorter.newLocals.length);
		assertEquals(0, sorter.newLocals[remapped]);
	}
	
	@Test
	public void testNewLocalMapping() {
		int local = sorter.newLocalMapping(type);
		
		assertNotNull(local);
		assertEquals(2, sorter.nextLocal);
	}
}