import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RobotRendererTest {

    @Test
    public void testRender() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Set<Integer> selectedRobotIds = Collections.singleton(1);
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert
        verify(g2d, never()).setPaint(any(Color.class));
        verify(g2d, never()).fill(any());
        verify(g2d, times(1)).setPaint(Color.white);
        verify(g2d, times(1)).draw(any());
    }

    @Test
    public void testIsRenderDead() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(false);

        // Act
        boolean result = robotRenderer.isRenderDead();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testSetRenderDead() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.setRenderDead(false);
        boolean result = robotRenderer.isRenderDead();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsShowStatusBars() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowStatusBars(false);

        // Act
        boolean result = robotRenderer.isShowStatusBars();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testSetShowStatusBars() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.setShowStatusBars(false);
        boolean result = robotRenderer.isShowStatusBars();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsShowName() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowName(false);

        // Act
        boolean result = robotRenderer.isShowName();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testSetShowName() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.setShowName(false);
        boolean result = robotRenderer.isShowName();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsFillShield() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(false);

        // Act
        boolean result = robotRenderer.isFillShield();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testSetFillShield() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.setFillShield(false);
        boolean result = robotRenderer.isFillShield();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testPaintSelection_WhenSelectedRobot() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        Set<Integer> selectedRobotIds = Collections.singleton(1);
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).fill(any());
        verify(g2d, never()).draw(any());
    }

    @Test
    public void testPaintShield_WhenActiveShield() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        when(robotSnapshot.getPositionVector()).thenReturn(new Point2D());
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

        // Assert
        verify(g2d, times(1)).setPaint(any(RadialGradientPaint.class));
        verify(g2d, times(2)).fill(any());
        verify(g2d, never()).draw(any());
    }

    @Test
    public void testPaintShield_WhenInactiveShield() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isActiveShield()).thenReturn(false);
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

        // Assert
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, never()).fill(any());
        verify(g2d, times(1)).draw(any());
    }

    @Test
    public void testPaintName() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.getName()).thenReturn("Test Robot");
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

        // Assert
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).drawString(eq("Test Robot"), anyFloat(), anyFloat());
    }

    @Test
    public void testPaintHeat_WhenRobotNotDead() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getTemperature()).thenReturn(new Temperature(50));
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

        // Assert
        verify(g2d, times(1)).setPaint(any(GradientPaint.class));
        verify(g2d, times(1)).fill(any(Rectangle2D.Double.class));
        verify(g2d, times(1)).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintHeat_WhenRobotDead() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.getTemperature()).thenReturn(new Temperature(50));
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

        // Assert
        verify(g2d, never()).setPaint(any(GradientPaint.class));
        verify(g2d, never()).fill(any(Rectangle2D.Double.class));
        verify(g2d, times(1)).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintArmor() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.getArmor()).thenReturn(50.0);
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

        // Assert
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).fill(any(Rectangle2D.Double.class));
        verify(g2d, times(1)).draw(any(Rectangle2D.Double.class));
    }

    @Test
    public void testPaintTurret() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.getTurretHeading()).thenReturn(new AbsoluteAngle());
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

        // Assert
        verify(g2d, times(1)).setPaint(any(Color.class));
        verify(g2d, times(1)).draw(any(Line2D.Double.class));
    }

    @Test
    public void testPaintBody_WhenRobotDead() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        when(robotSnapshot.isDead()).thenReturn(true);
        robotSnapshot.setHeading(new AbsoluteAngle());
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

        // Assert
        verify(g2d, times(1)).draw(any(GeneralPath.class));
        verify(g2d, never()).fill(any(GeneralPath.class));
    }

    @Test
    public void testPaintBody_WhenRobotAlive() {
        // Arrange
        Graphics2D g2d = mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = mock(RobotSnapshot.class);
        robotSnapshot.setHeading(new AbsoluteAngle());
        RobotRenderer robotRenderer = new RobotRenderer();

        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());

        // Assert
        verify(g2d, never()).draw(any(GeneralPath.class));
        verify(g2d, times(1)).fill(any(GeneralPath.class));
    }

}