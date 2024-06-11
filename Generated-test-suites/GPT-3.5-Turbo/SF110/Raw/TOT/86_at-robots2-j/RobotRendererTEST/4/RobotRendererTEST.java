import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RobotRendererTest {
    private RobotRenderer robotRenderer;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setUp() {
        robotRenderer = new RobotRenderer();
        robotSnapshot = new RobotSnapshot();
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRender_deadRobotNotRendered_shouldNotRenderBodyAndOtherComponents() {
        robotRenderer.setRenderDead(false);
        robotSnapshot.setDead(true);

        Graphics2D g2d = mock(Graphics2D.class);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that body is not painted
        // ...

        // Assert that other components are not painted
        // ...
    }

    @Test
    public void testRender_deadRobotRendered_shouldRenderBodyAndOtherComponents() {
        robotRenderer.setRenderDead(true);
        robotSnapshot.setDead(true);

        Graphics2D g2d = mock(Graphics2D.class);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        // Assert that body is painted
        // ...

        // Assert that other components are painted
        // ...
    }

    @Test
    public void testRender_activeShieldFillShieldEnabled_shouldFillShield() {
        robotRenderer.setFillShield(true);
        robotSnapshot.setActiveShield(true);

        Graphics2D g2d = mock(Graphics2D.class);
        robotRenderer.paintShield(g2d, robotSnapshot);

        // Assert that shield is filled
        // ...
    }

    @Test
    public void testRender_activeShieldFillShieldDisabled_shouldDrawShield() {
        robotRenderer.setFillShield(false);
        robotSnapshot.setActiveShield(true);

        Graphics2D g2d = mock(Graphics2D.class);
        robotRenderer.paintShield(g2d, robotSnapshot);

        // Assert that shield is drawn
        // ...
    }

    @Test
    public void testRender_inactiveShieldFillShieldEnabled_shouldNotFillShield() {
        robotRenderer.setFillShield(true);
        robotSnapshot.setActiveShield(false);

        Graphics2D g2d = mock(Graphics2D.class);
        robotRenderer.paintShield(g2d, robotSnapshot);

        // Assert that shield is not filled
        // ...
    }

    @Test
    public void testRender_inactiveShieldFillShieldDisabled_shouldNotDrawShield() {
        robotRenderer.setFillShield(false);
        robotSnapshot.setActiveShield(false);

        Graphics2D g2d = mock(Graphics2D.class);
        robotRenderer.paintShield(g2d, robotSnapshot);

        // Assert that shield is not drawn
        // ...
    }

    // Repeat similar test cases for other methods

}