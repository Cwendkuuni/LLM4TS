import static org.junit.Assert.*;
import org.junit.Test;

public class PlotAxisTest {

	@Test
	public void testSetLabel() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.setLabel("Test Label");
		assertEquals("Test Label", axis.getLabel());
	}

	@Test
	public void testSetScale() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.setScale(new LinearAxisScale());
		assertTrue(axis.getScale() instanceof LinearAxisScale);
	}

	@Test
	public void testSetNumberFormat() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.setNumberFormat(NumberFormat.getInstance());
		assertNotNull(axis.getNumberFormat());
	}

	@Test
	public void testSetGridStyle() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.setGridStyle(2);
		assertEquals(2, axis.getGridStyle());
	}

	@Test
	public void testManualLowerBounds() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.manualLowerBounds(true);
	}

	@Test
	public void testSetLowerBounds() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.setLowerBounds(1.0);
		assertEquals(1.0, axis.getLowerBounds(), 0);
	}

	@Test
	public void testLowerBoundsAsString() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.setLowerBounds(1.0);
		assertEquals("1.0", axis.lowerBoundsAsString());
	}

	@Test
	public void testManualUpperBounds() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.manualUpperBounds(true);
	}

	@Test
	public void testSetUpperBounds() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.setUpperBounds(1.0);
		assertEquals(1.0, axis.getUpperBounds(), 0);
	}

	@Test
	public void testUpperBoundsAsString() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.setUpperBounds(1.0);
		assertEquals("1.0", axis.upperBoundsAsString());
	}

	@Test
	public void testManualTickStepSize() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.manualTickStepSize(true);
	}

	@Test
	public void testSetTickStepSize() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		axis.setTickStepSize(1.0);
		assertEquals(1.0, axis.getTickStepSize(), 0);
	}

	@Test
	public void testScaleCoord() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		assertEquals(0, axis.scaleCoord(0.0));
	}

	@Test
	public void testClone() {
		PlotAxis axis = new PlotAxis() {
			@Override
			public void setAxisBounds(PlotRunList p0) {
			}

			@Override
			protected void resizeAxis(Rectangle p0) {
			}

			@Override
			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		PlotAxis clonedAxis = (PlotAxis) axis.clone();
		assertNotNull(clonedAxis);
	}

}