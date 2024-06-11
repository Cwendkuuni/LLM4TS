package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.*;
import org.junit.Test;

public class IFXObjectTest {

	@Test
	public void testEquals() {
		IFXObject obj1 = new IFXObject();
		IFXObject obj2 = new IFXObject();
		
		boolean result = obj1.equals(obj2);
		
		assertTrue(result);
	}

	@Test
	public void testToString() {
		IFXObject obj = new IFXObject();
		
		String result = obj.toString();
		
		assertNotNull(result);
	}

	@Test
	public void testStringify() {
		IFXObject obj = new IFXObject();
		int level = 0;
		
		String result = obj.stringify(obj, level);
		
		assertNotNull(result);
	}

	@Test
	public void testDoCheckEquals() {
		IFXObject thisObj = new IFXObject();
		IFXObject thatObj = new IFXObject();
		
		boolean result = thisObj.doCheckEquals(thisObj, thatObj);
		
		assertTrue(result);
	}

	@Test
	public void testTab() {
		IFXObject obj = new IFXObject();
		int level = 0;
		
		String result = obj.tab(level);
		
		assertNotNull(result);
	}

}