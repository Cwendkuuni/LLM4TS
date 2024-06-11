import org.junit.Test;
import java.awt.*;
import java.awt.geom.*;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class RobotRendererTest {

  @Test
  public void testRender() {
    RobotRenderer robotRenderer = new RobotRenderer();
    
    Graphics2D g2d = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    Set<Integer> selectedRobotIds = new HashSet<Integer>();
    
    robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
  }

  @Test
  public void testIsRenderDead() {
    RobotRenderer robotRenderer = new RobotRenderer();
    boolean isRenderDead = robotRenderer.isRenderDead();
    assertEquals(true, isRenderDead);
  }

  @Test
  public void testSetRenderDead() {
    RobotRenderer robotRenderer = new RobotRenderer();
    robotRenderer.setRenderDead(true);
    boolean isRenderDead = robotRenderer.isRenderDead();
    assertEquals(true, isRenderDead);
  }

  @Test
  public void testIsShowStatusBars() {
    RobotRenderer robotRenderer = new RobotRenderer();
    boolean isShowStatusBars = robotRenderer.isShowStatusBars();
    assertEquals(true, isShowStatusBars);
  }

  @Test
  public void testSetShowStatusBars() {
    RobotRenderer robotRenderer = new RobotRenderer();
    robotRenderer.setShowStatusBars(true);
    boolean isShowStatusBars = robotRenderer.isShowStatusBars();
    assertEquals(true, isShowStatusBars);
  }

  @Test
  public void testIsShowName() {
    RobotRenderer robotRenderer = new RobotRenderer();
    boolean isShowName = robotRenderer.isShowName();
    assertEquals(true, isShowName);
  }

  @Test
  public void testSetShowName() {
    RobotRenderer robotRenderer = new RobotRenderer();
    robotRenderer.setShowName(true);
    boolean isShowName = robotRenderer.isShowName();
    assertEquals(true, isShowName);
  }

  @Test
  public void testIsFillShield() {
    RobotRenderer robotRenderer = new RobotRenderer();
    boolean isFillShield = robotRenderer.isFillShield();
    assertEquals(true, isFillShield);
  }

  @Test
  public void testSetFillShield() {
    RobotRenderer robotRenderer = new RobotRenderer();
    robotRenderer.setFillShield(true);
    boolean isFillShield = robotRenderer.isFillShield();
    assertEquals(true, isFillShield);
  }

  @Test
  public void testPaintSelection() {
    RobotRenderer robotRenderer = new RobotRenderer();
    Graphics2D g2d = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setPositionVector(new Vector(50, 50));
    
    robotRenderer.paintSelection(g2d, robotSnapshot);
  }

  @Test
  public void testPaintShield() {
    RobotRenderer robotRenderer = new RobotRenderer();
    Graphics2D g2d = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setActiveShield(true);
    robotSnapshot.setPositionVector(new Vector(50, 50));

    robotRenderer.paintShield(g2d, robotSnapshot);
  }

  @Test
  public void testPaintName() {
    RobotRenderer robotRenderer = new RobotRenderer();
    Graphics2D g2d = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setName("Robot 1");
    robotSnapshot.setPositionVector(new Vector(50, 50));

    robotRenderer.paintName(g2d, robotSnapshot);
  }

  @Test
  public void testPaintHeat() {
    RobotRenderer robotRenderer = new RobotRenderer();
    Graphics2D g2d = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setX(50);
    robotSnapshot.setY(50);
    robotSnapshot.setTemperature(new Temperature(75.0));

    robotRenderer.paintHeat(g2d, robotSnapshot);
  }

  @Test
  public void testPaintArmor() {
    RobotRenderer robotRenderer = new RobotRenderer();
    Graphics2D g2d = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setX(50);
    robotSnapshot.setY(50);
    robotSnapshot.setArmor(75.0);

    robotRenderer.paintArmor(g2d, robotSnapshot);
  }

  @Test
  public void testPaintTurret() {
    RobotRenderer robotRenderer = new RobotRenderer();
    Graphics2D g2d = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setX(50);
    robotSnapshot.setY(50);
    robotSnapshot.setTurretHeading(new Angle(90.0));

    robotRenderer.paintTurret(g2d, robotSnapshot);
  }

  @Test
  public void testPaintBody() {
    RobotRenderer robotRenderer = new RobotRenderer();
    Graphics2D g2d = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
    RobotSnapshot robotSnapshot = new RobotSnapshot();
    robotSnapshot.setX(50);
    robotSnapshot.setY(50);
    robotSnapshot.setHeading(new Angle(90.0));

    robotRenderer.paintBody(g2d, robotSnapshot);
  }

}