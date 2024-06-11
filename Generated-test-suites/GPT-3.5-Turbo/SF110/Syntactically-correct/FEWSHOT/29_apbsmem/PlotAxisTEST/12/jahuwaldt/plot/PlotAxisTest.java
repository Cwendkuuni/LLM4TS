package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.StringWriter;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {
	
	@Test
	public void testConstructor() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		assertNull(plotAxis.getLabel());
		assertTrue(plotAxis.getScale() instanceof LinearAxisScale);
		assertTrue(plotAxis.getNumberFormat() instanceof NumberFormat);
		assertEquals(1, plotAxis.getGridStyle());
	}
	
	@Test
	public void testSetLabel() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.setLabel("label");
		assertEquals("label", plotAxis.getLabel());
	}
	
	@Test
	public void testSetScale() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.setScale(new LinearAxisScale());
		assertTrue(plotAxis.getScale() instanceof LinearAxisScale);
	}
	
	@Test
	public void testSetNumberFormat() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.setNumberFormat(NumberFormat.getInstance());
		assertTrue(plotAxis.getNumberFormat() instanceof NumberFormat);
	}
	
	@Test
	public void testSetGridStyle() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.setGridStyle(2);
		assertEquals(2, plotAxis.getGridStyle());
	}
	
	@Test
	public void testManualLowerBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.manualLowerBounds(true);
	}
	
	@Test
	public void testSetLowerBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.setLowerBounds(1.0);
		assertEquals(1.0, plotAxis.getLowerBounds(), 0.0);
	}
	
	@Test
	public void testGetLowerBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0.0);
	}
	
	@Test
	public void testLowerBoundsAsString() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		assertEquals("1.4E-45", plotAxis.lowerBoundsAsString());
	}
	
	@Test
	public void testManualUpperBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.manualUpperBounds(true);
	}
	
	@Test
	public void testSetUpperBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.setUpperBounds(1.0);
		assertEquals(1.0, plotAxis.getUpperBounds(), 0.0);
	}
	
	@Test
	public void testGetUpperBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0.0);
	}
	
	@Test
	public void testUpperBoundsAsString() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		assertEquals("-1.7976931348623157E308", plotAxis.upperBoundsAsString());
	}
	
	@Test
	public void testManualTickStepSize() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.manualTickStepSize(true);
	}
	
	@Test
	public void testSetTickStepSize() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.setTickStepSize(1.0);
		assertEquals(1.0, plotAxis.getTickStepSize(), 0.0);
	}
	
	@Test
	public void testGetTickStepSize() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		assertEquals(1.0, plotAxis.getTickStepSize(), 0.0);
	}
	
	@Test
	public void testSetAxisBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.setAxisBounds(new PlotRunList());
	}
	
	@Test
	public void testSetSize() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			protected void setAxisTickLimits() {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		plotAxis.setSize(new Rectangle());
	}
	
	@Test
	public void testDraw() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			protected void setAxisTickLimits() {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void setAxisBounds(PlotRunList p0) {}
		};
		plotAxis.draw(new Graphics(), new Component(), new Rectangle());
	}
	
	@Test
	public void testScaleCoord() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			protected void setAxisTickLimits() {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		assertEquals(0, plotAxis.scaleCoord(0.0));
	}
	
	@Test
	public void testClone() {
		PlotAxis plotAxis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {}
			@Override
			protected void resizeAxis(Rectangle p0) {}
			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {}
		};
		PlotAxis clone = (PlotAxis)plotAxis.clone();
		assertNull(clone.getLabel());
		assertTrue(clone.getScale() instanceof LinearAxisScale);
		assertTrue(clone.getNumberFormat() instanceof NumberFormat);
		assertEquals(1, clone.getGridStyle());
	}
}