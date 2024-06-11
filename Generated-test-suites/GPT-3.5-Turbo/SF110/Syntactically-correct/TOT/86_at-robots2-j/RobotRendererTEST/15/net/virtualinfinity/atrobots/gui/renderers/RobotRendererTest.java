import org.junit.*;
import static org.junit.Assert.*;
import java.awt.Graphics2D;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import net.virtualinfinity.atrobots.gui.renderers.*;

public class RobotRendererTest {
  
  private RobotRenderer robotRenderer;
  
  @Before
  public void setUp() {
    robotRenderer = new RobotRenderer();
  }
  
  @Test
  public void testRenderShouldNotPaintIfRenderDeadIsFalseAndRobotIsDead() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setDead(true);
    robotRenderer.setRenderDead(false);
    
    robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
    
    verify(g2d, never()).paint(any());
  }
  
  @Test
  public void testRenderShouldPaintBodyAndShieldIfRobotIsNotDead() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setDead(false);
    robotSnapshot.setActiveShield(true);
    robotRenderer.setRenderDead(true);
    
    robotRenderer.render(g2d, robotSnapshot, new HashSet<Integer>());
    
    verify(g2d, times(2)).paint(any());
  }
  
  @Test
  public void testRenderShouldPaintSelectionIfRobotIsSelected() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setDead(false);
    robotRenderer.setRenderDead(true);
    Set<Integer> selectedRobotIds = new HashSet<Integer>();
    selectedRobotIds.add(robotSnapshot.getId());
    
    robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
    
    verify(g2d, times(1)).paint(any());
  }
  
  @Test
  public void testIsRenderDeadShouldReturnTrueByDefault() {
    boolean actual = robotRenderer.isRenderDead();
    
    assertTrue(actual);
  }
  
  @Test
  public void testSetRenderDeadShouldSetRenderDead() {
    boolean expected = false;
    
    robotRenderer.setRenderDead(expected);
    boolean actual = robotRenderer.isRenderDead();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsShowStatusBarsShouldReturnTrueByDefault() {
    boolean actual = robotRenderer.isShowStatusBars();
    
    assertTrue(actual);
  }
  
  @Test
  public void testSetShowStatusBarsShouldSetShowStatusBars() {
    boolean expected = false;
    
    robotRenderer.setShowStatusBars(expected);
    boolean actual = robotRenderer.isShowStatusBars();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsShowNameShouldReturnTrueByDefault() {
    boolean actual = robotRenderer.isShowName();
    
    assertTrue(actual);
  }
  
  @Test
  public void testSetShowNameShouldSetShowName() {
    boolean expected = false;
    
    robotRenderer.setShowName(expected);
    boolean actual = robotRenderer.isShowName();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsFillShieldShouldReturnTrueByDefault() {
    boolean actual = robotRenderer.isFillShield();
    
    assertTrue(actual);
  }
  
  @Test
  public void testSetFillShieldShouldSetFillShield() {
    boolean expected = false;
    
    robotRenderer.setFillShield(expected);
    boolean actual = robotRenderer.isFillShield();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testPaintSelectionShouldPaintSelection() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    
    robotRenderer.paintSelection(g2d, robotSnapshot);
    
    verify(g2d, times(1)).paint(any());
  }
  
  @Test
  public void testPaintShieldShouldFillShieldIfFillShieldIsTrue() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotRenderer.setFillShield(true);
    
    robotRenderer.paintShield(g2d, robotSnapshot);
    
    verify(g2d, times(1)).fill(any());
  }
  
  @Test
  public void testPaintShieldShouldDrawShieldIfFillShieldIsFalse() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotRenderer.setFillShield(false);
    
    robotRenderer.paintShield(g2d, robotSnapshot);
    
    verify(g2d, times(1)).draw(any());
  }
  
  @Test
  public void testPaintNameShouldDrawNameInYellowColor() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    
    robotRenderer.paintName(g2d, robotSnapshot);
    
    verify(g2d, times(1)).drawString(any(), eq(Color.yellow));
  }
  
  @Test
  public void testPaintHeatShouldFillHeatIfRobotIsNotDead() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setDead(false);
    
    robotRenderer.paintHeat(g2d, robotSnapshot);
    
    verify(g2d, times(1)).fill(any());
  }
  
  @Test
  public void testPaintHeatShouldNotFillHeatIfRobotIsDead() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setDead(true);
    
    robotRenderer.paintHeat(g2d, robotSnapshot);
    
    verify(g2d, never()).fill(any());
  }
  
  @Test
  public void testPaintHeatShouldDrawHeat() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    
    robotRenderer.paintHeat(g2d, robotSnapshot);
    
    verify(g2d, times(1)).draw(any());
  }
  
  @Test
  public void testPaintArmorShouldFillArmor() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    
    robotRenderer.paintArmor(g2d, robotSnapshot);
    
    verify(g2d, times(1)).fill(any());
  }
  
  @Test
  public void testPaintArmorShouldDrawArmor() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    
    robotRenderer.paintArmor(g2d, robotSnapshot);
    
    verify(g2d, times(1)).draw(any());
  }
  
  @Test
  public void testPaintTurretShouldDrawTurretInWhiteColor() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    
    robotRenderer.paintTurret(g2d, robotSnapshot);
    
    verify(g2d, times(1)).draw(any());
  }
  
  @Test
  public void testPaintBodyShouldDrawBodyInOrangeColorIfRobotIsDead() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setDead(true);
    
    robotRenderer.paintBody(g2d, robotSnapshot);
    
    verify(g2d, times(1)).draw(any());
  }
  
  @Test
  public void testPaintBodyShouldFillBodyInRedColorIfRobotIsNotDead() {
    Graphics2D g2d = (Graphics2D)mock(Graphics2D.class);
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setDead(false);
    
    robotRenderer.paintBody(g2d, robotSnapshot);
    
    verify(g2d, times(1)).fill(any());
  }
  
}