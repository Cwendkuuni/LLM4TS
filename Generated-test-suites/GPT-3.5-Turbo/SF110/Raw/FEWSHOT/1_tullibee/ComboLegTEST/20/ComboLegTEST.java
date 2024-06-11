package com.ib.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class ComboLegTest {

	@Test
	public void testDefaultConstructor() {
		ComboLeg c = new ComboLeg();

		assertEquals(0, c.m_conId);
		assertEquals(0, c.m_ratio);
		assertNull(c.m_action);
		assertNull(c.m_exchange);
		assertEquals(0, c.m_openClose);
		assertEquals(0, c.m_shortSaleSlot);
		assertNull(c.m_designatedLocation);
	}

	@Test
	public void testConstructorWithParameters() {
		ComboLeg c = new ComboLeg(123, 2, "Buy", "NYSE", 1);

		assertEquals(123, c.m_conId);
		assertEquals(2, c.m_ratio);
		assertEquals("Buy", c.m_action);
		assertEquals("NYSE", c.m_exchange);
		assertEquals(1, c.m_openClose);
		assertEquals(0, c.m_shortSaleSlot);
		assertNull(c.m_designatedLocation);
	}

	@Test
	public void testFullConstructor() {
		ComboLeg c = new ComboLeg(123, 2, "Buy", "NYSE", 1, 2, "Location");

		assertEquals(123, c.m_conId);
		assertEquals(2, c.m_ratio);
		assertEquals("Buy", c.m_action);
		assertEquals("NYSE", c.m_exchange);
		assertEquals(1, c.m_openClose);
		assertEquals(2, c.m_shortSaleSlot);
		assertEquals("Location", c.m_designatedLocation);
	}

	@Test
	public void testEquals() {
		ComboLeg c1 = new ComboLeg(123, 2, "Buy", "NYSE", 1, 2, "Location");
		ComboLeg c2 = new ComboLeg(123, 2, "Buy", "NYSE", 1, 2, "Location");

		assertTrue(c1.equals(c2));
	}

	@Test
	public void testEquals_SameObject() {
		ComboLeg c1 = new ComboLeg(123, 2, "Buy", "NYSE", 1, 2, "Location");

		assertTrue(c1.equals(c1));
	}

	@Test
	public void testEquals_NullObject() {
		ComboLeg c1 = new ComboLeg(123, 2, "Buy", "NYSE", 1, 2, "Location");

		assertFalse(c1.equals(null));
	}

	@Test
	public void testEquals_DifferentObject() {
		ComboLeg c1 = new ComboLeg(123, 2, "Buy", "NYSE", 1, 2, "Location");
		ComboLeg c2 = new ComboLeg(456, 3, "Sell", "NASDAQ", 0, 1, "Location");

		assertFalse(c1.equals(c2));
	}

}