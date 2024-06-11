package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

	private PlotAxis plotAxis;

	@Before
	public void setUp() {
		plotAxis = new PlotAxisImpl();
	}

	@Test
	public void testSetLabel() {
		plotAxis.setLabel("Test Label");
		assertEquals("Test Label", plotAxis.getLabel());
	}

	@Test
	public void testGetLabel() {
		plotAxis.setLabel("Test Label");
		assertEquals("Test Label", plotAxis.getLabel());
	}

	@Test
	public void testSetScale() {
		PlotAxisScale scaleMock = mock(PlotAxisScale.class);
		plotAxis.setScale(scaleMock);
		assertEquals(scaleMock, plotAxis.getScale());
	}

	@Test
	public void testGetScale() {
		PlotAxisScale scaleMock = mock(PlotAxisScale.class);
		plotAxis.setScale(scaleMock);
		assertEquals(scaleMock, plotAxis.getScale());
	}

	@Test
	public void testSetNumberFormat() {
		NumberFormat formatMock = mock(NumberFormat.class);
		plotAxis.setNumberFormat(formatMock);
		assertEquals(formatMock, plotAxis.getNumberFormat());
	}

	@Test
	public void testGetNumberFormat() {
		NumberFormat formatMock = mock(NumberFormat.class);
		plotAxis.setNumberFormat(formatMock);
		assertEquals(formatMock, plotAxis.getNumberFormat());
	}

	@Test
	public void testSetGridStyle() {
		plotAxis.setGridStyle(2);
		assertEquals(2, plotAxis.getGridStyle());
	}

	@Test
	public void testGetGridStyle() {
		plotAxis.setGridStyle(2);
		assertEquals(2, plotAxis.getGridStyle());
	}

	@Test
	public void testManualLowerBounds() {
		plotAxis.manualLowerBounds(true);
	}

	@Test
	public void testSetLowerBounds() {
		plotAxis.setLowerBounds(10.5);
		assertEquals(10.5, plotAxis.getLowerBounds(), 0.0001);
	}

	@Test
	public void testGetLowerBounds() {
		plotAxis.setLowerBounds(10.5);
		assertEquals(10.5, plotAxis.getLowerBounds(), 0.0001);
	}

	@Test
	public void testLowerBoundsAsString() {
		plotAxis.setLowerBounds(10.5);
		assertEquals("10.5", plotAxis.lowerBoundsAsString());
	}

	@Test
	public void testManualUpperBounds() {
		plotAxis.manualUpperBounds(true);
	}

	@Test
	public void testSetUpperBounds() {
		plotAxis.setUpperBounds(20.5);
		assertEquals(20.5, plotAxis.getUpperBounds(), 0.0001);
	}

	@Test
	public void testGetUpperBounds() {
		plotAxis.setUpperBounds(20.5);
		assertEquals(20.5, plotAxis.getUpperBounds(), 0.0001);
	}

	@Test
	public void testUpperBoundsAsString() {
		plotAxis.setUpperBounds(20.5);
		assertEquals("20.5", plotAxis.upperBoundsAsString());
	}

	@Test
	public void testManualTickStepSize() {
		plotAxis.manualTickStepSize(true);
	}

	@Test
	public void testSetTickStepSize() {
		plotAxis.setTickStepSize(0.5);
		assertEquals(0.5, plotAxis.getTickStepSize(), 0.0001);
	}

	@Test
	public void testGetTickStepSize() {
		plotAxis.setTickStepSize(0.5);
		assertEquals(0.5, plotAxis.getTickStepSize(), 0.0001);
	}

	@Test
	public void testSetAxisBounds() {
		PlotRunList plotRunListMock = mock(PlotRunList.class);
		plotAxis.setAxisBounds(plotRunListMock);
		verify(plotRunListMock, times(1)).getLowerBound();
		verify(plotRunListMock, times(1)).getUpperBound();
	}

	@Test
	public void testSetSize() {
		Rectangle plotFrameMock = mock(Rectangle.class);
		plotAxis.setSize(plotFrameMock);
		verify(plotAxis, times(1)).setAxisTickLimits();
		verify(plotAxis, times(1)).resizeAxis(plotFrameMock);
	}

	@Test
	public void testDraw() {
		Graphics graphicsMock = mock(Graphics.class);
		Component componentMock = mock(Component.class);
		Rectangle rectangleMock = mock(Rectangle.class);
		plotAxis.draw(graphicsMock, componentMock, rectangleMock);
	}

	@Test
	public void testScaleCoord() {
		plotAxis.scaleCoord(10.0);
	}

	@Test
	public void testClone() {
		PlotAxis clone = (PlotAxis) plotAxis.clone();
		assertEquals(plotAxis, clone);
	}

	private class PlotAxisImpl extends PlotAxis {
		@Override
		public void setAxisBounds(PlotRunList p0) {
		}

		@Override
		protected void resizeAxis(Rectangle p0) {
		}

		@Override
		public void draw(Graphics p0, Component p1, Rectangle p2) {
		}
	}
}