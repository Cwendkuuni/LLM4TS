import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;

public class DefaultChartTest {
  
  @Mock
  private Graphics2D mockGraphics;
  
  private DefaultChart defaultChart;
  private ChartDataModel chartDataModel;
  private RowColorModel rowColorModel;
  private Legend legend;
  private Title title;
  private CoordSystem coordSystem;
  
  @Before
  public void setup() {
    chartDataModel = new ChartDataModel();
    rowColorModel = new RowColorModel(chartDataModel);
    legend = new Legend(rowColorModel);
    title = new Title("Test Chart");
    coordSystem = new CoordSystem(chartDataModel);
    defaultChart = new DefaultChart(chartDataModel, "Test Chart");
    defaultChart.setRowColorModel(rowColorModel);
    defaultChart.setLegend(legend);
    defaultChart.setTitle(title);
    defaultChart.setCoordSystem(coordSystem);
  }
  
  @Test
  public void testConstructor1() {
    DefaultChart chart = new DefaultChart(chartDataModel, "Test Chart");
    assertEquals(chartDataModel, chart.getChartDataModel());
    assertEquals(rowColorModel, chart.getRowColorModel());
    assertEquals(legend, chart.getLegend());
    assertEquals(title, chart.getTitle());
    assertNull(chart.getCoordSystem());
  }
  
  @Test
  public void testConstructor2() {
    DefaultChart chart = new DefaultChart(chartDataModel, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
    assertEquals(chartDataModel, chart.getChartDataModel());
    assertEquals(rowColorModel, chart.getRowColorModel());
    assertEquals(legend, chart.getLegend());
    assertEquals(title, chart.getTitle());
    assertEquals(coordSystem, chart.getCoordSystem());
  }
  
  @Test
  public void testConstructor3() {
    DefaultChart chart = new DefaultChart(chartDataModel, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X", "Y");
    assertEquals(chartDataModel, chart.getChartDataModel());
    assertEquals(rowColorModel, chart.getRowColorModel());
    assertEquals(legend, chart.getLegend());
    assertEquals(title, chart.getTitle());
    assertEquals(coordSystem, chart.getCoordSystem());
    assertEquals("X", coordSystem.getXAxisUnit());
    assertEquals("Y", coordSystem.getYAxisUnit());
  }
  
  @Test
  public void testGetPreferredSize() {
    Dimension preferredSize = defaultChart.getPreferredSize();
    assertNull(preferredSize);
  }
  
  @Test
  public void testRender() {
    defaultChart.render(mockGraphics);
    
    assertEquals(Color.white, mockGraphics.getColor());
    assertEquals(0, mockGraphics.getFillCalls());
    assertEquals(Color.black, mockGraphics.getSetColor());
    assertEquals(0, mockGraphics.getFillRectCalls());
    assertEquals(0, mockGraphics.getDrawRectCalls());
    assertEquals(0, mockGraphics.getDrawStringCalls());
    assertEquals(0, mockGraphics.getSetFontCalls());
    assertEquals(0, mockGraphics.getDrawLineCalls());
    assertEquals(0, mockGraphics.getDrawPolylineCalls());
    assertEquals(0, mockGraphics.getDrawOvalCalls());
    assertEquals(0, mockGraphics.getDrawArcCalls());
    assertEquals(0, mockGraphics.getDrawRoundRectCalls());
    assertEquals(0, mockGraphics.getDrawImageCalls());
    assertEquals(0, mockGraphics.getRenderCalls());
  }

  @Test
  public void testStaticFields() {
    assertEquals(0, DefaultChart.LINEAR_X_LINEAR_Y);
    assertEquals(1, DefaultChart.NO_COORDINATE_SYSTEM);
  }
}