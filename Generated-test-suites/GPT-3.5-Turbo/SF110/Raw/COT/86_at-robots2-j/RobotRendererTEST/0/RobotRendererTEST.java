import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class RobotRendererTest {
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;
    private Graphics2D g2d;
    private RobotRenderer robotRenderer;

    @Before
    public void setUp() {
        robotSnapshot = mock(RobotSnapshot.class);
        selectedRobotIds = new HashSet<>();
        g2d = mock(Graphics2D.class);
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRender_aliveRobot_renderDeadFalse() {
        // Arrange
        when(robotSnapshot.isDead()).thenReturn(false);
        robotRenderer.setRenderDead(false);

        // Act
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert
        verify(g2d, times(1)).setPaint(Color.red);
        // Assert other paint methods were called with correct arguments
    }

    @Test
    public void testRender_deadRobot_renderDeadFalse() {
        // Arrange
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.setRenderDead(false);

        // Act
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert
        verify(g2d, never()).setPaint(Color.orange.darker().darker());
        // Assert other paint methods were not called
    }

    @Test
    public void testRender_deadRobot_showStatusBarsTrue() {
        // Arrange
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.setShowStatusBars(true);

        // Act
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert
        verify(g2d, never()).fill(any(Rectangle2D.Double.class));
        // Assert other paint methods were called with correct arguments
    }

    @Test
    public void testRender_deadRobot_showNameTrue() {
        // Arrange
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.setShowName(true);

        // Act
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert
        verify(g2d, never()).drawString(anyString(), anyFloat(), anyFloat());
        // Assert other paint methods were called with correct arguments
    }

    @Test
    public void testRender_deadRobot_fillShieldFalse() {
        // Arrange
        when(robotSnapshot.isDead()).thenReturn(true);
        robotRenderer.setFillShield(false);

        // Act
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert
        verify(g2d, times(1)).setPaint(new Color(0.8f, 0.6f, 1.0f));
        // Assert other paint methods were called with correct arguments
    }

    @Test
    public void testRender_selectedRobotIdsContainRobotId() {
        // Arrange
        selectedRobotIds.add(robotSnapshot.getId());

        // Act
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert
        verify(g2d, times(1)).fill(any(Ellipse2D.Double.class));
        // Assert other paint methods were called with correct arguments
    }
}