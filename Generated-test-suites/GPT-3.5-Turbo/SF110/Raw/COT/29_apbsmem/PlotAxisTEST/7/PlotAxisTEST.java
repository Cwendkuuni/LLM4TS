import static org.junit.Assert.*;
import org.junit.Test;

public class PlotAxisTest {

	@Test
	public void testSetLabel() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setLabel("Test Label");
		assertEquals("Test Label", plotAxis.getLabel());
	}

	@Test
	public void testGetLabel() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setLabel("Test Label");
		assertEquals("Test Label", plotAxis.getLabel());
	}

	@Test
	public void testSetScale() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setScale(new LinearAxisScale());
		assertEquals(LinearAxisScale.class, plotAxis.getScale().getClass());
	}

	@Test
	public void testGetScale() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setScale(new LinearAxisScale());
		assertEquals(LinearAxisScale.class, plotAxis.getScale().getClass());
	}

	@Test
	public void testSetNumberFormat() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setNumberFormat(NumberFormat.getCurrencyInstance());
		assertEquals(NumberFormat.getCurrencyInstance(), plotAxis.getNumberFormat());
	}

	@Test
	public void testGetNumberFormat() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setNumberFormat(NumberFormat.getCurrencyInstance());
		assertEquals(NumberFormat.getCurrencyInstance(), plotAxis.getNumberFormat());
	}

	@Test
	public void testSetGridStyle() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setGridStyle(PlotAxis.kMajorGrid);
		assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
	}

	@Test
	public void testGetGridStyle() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setGridStyle(PlotAxis.kMajorGrid);
		assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
	}

	@Test
	public void testManualLowerBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.manualLowerBounds(true);
	}

	@Test
	public void testSetLowerBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setLowerBounds(10.5);
		assertEquals(10.5, plotAxis.getLowerBounds(), 0);
	}

	@Test
	public void testGetLowerBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setLowerBounds(10.5);
		assertEquals(10.5, plotAxis.getLowerBounds(), 0);
	}

	@Test
	public void testLowerBoundsAsString() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setLowerBounds(10.5);
		assertEquals("10.5", plotAxis.lowerBoundsAsString());
	}

	@Test
	public void testManualUpperBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.manualUpperBounds(true);
	}

	@Test
	public void testSetUpperBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setUpperBounds(100.5);
		assertEquals(100.5, plotAxis.getUpperBounds(), 0);
	}

	@Test
	public void testGetUpperBounds() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setUpperBounds(100.5);
		assertEquals(100.5, plotAxis.getUpperBounds(), 0);
	}

	@Test
	public void testUpperBoundsAsString() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setUpperBounds(100.5);
		assertEquals("100.5", plotAxis.upperBoundsAsString());
	}

	@Test
	public void testManualTickStepSize() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.manualTickStepSize(true);
	}

	@Test
	public void testSetTickStepSize() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setTickStepSize(0.5);
		assertEquals(0.5, plotAxis.getTickStepSize(), 0);
	}

	@Test
	public void testGetTickStepSize() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void resizeAxis(Rectangle p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}
		};
		plotAxis.setTickStepSize(0.5);
		assertEquals(0.5, plotAxis.getTickStepSize(), 0);
	}

	@Test
	public void testResizeAxis() {
		PlotAxis plotAxis = new PlotAxis() {
			public void setAxisBounds(PlotRunList p0) {
			}

			public void draw(Graphics p0, Component p1, Rectangle p2) {
			}

			protected void resizeAxis(Rectangle p0) {
				assertEquals(100, p0.width);
				assertEquals(200, p0.height);
			}
		};
		plotAxis.setSize(new Rectangle(100, 200));
	}

}