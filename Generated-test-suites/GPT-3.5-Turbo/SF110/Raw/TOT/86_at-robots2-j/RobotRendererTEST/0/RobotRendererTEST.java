import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import java.awt.*;
import java.util.*;

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
    public void test_Render_ShowStatusBars_True() {
        robotRenderer.setShowStatusBars(true);
        Graphics2D g2d = new Graphics2D();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that paintHeat() and paintArmor() methods are called
        assertTrue(g2d.isPaintHeatCalled());
        assertTrue(g2d.isPaintArmorCalled());
    }

    @Test
    public void test_Render_ShowStatusBars_False() {
        robotRenderer.setShowStatusBars(false);
        Graphics2D g2d = new Graphics2D();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that paintHeat() and paintArmor() methods are not called
        assertFalse(g2d.isPaintHeatCalled());
        assertFalse(g2d.isPaintArmorCalled());
    }

    @Test
    public void test_Render_ShowName_True() {
        robotRenderer.setShowName(true);
        Graphics2D g2d = new Graphics2D();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that paintName() method is called
        assertTrue(g2d.isPaintNameCalled());
    }

    @Test
    public void test_Render_ShowName_False() {
        robotRenderer.setShowName(false);
        Graphics2D g2d = new Graphics2D();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that paintName() method is not called
        assertFalse(g2d.isPaintNameCalled());
    }

    @Test
    public void test_Render_RenderDead_True() {
        robotRenderer.setRenderDead(true);
        robotSnapshot.setDead(true);
        Graphics2D g2d = new Graphics2D();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that paintBody() method is not called
        assertFalse(g2d.isPaintBodyCalled());
    }

    @Test
    public void test_Render_RenderDead_False() {
        robotRenderer.setRenderDead(false);
        robotSnapshot.setDead(true);
        Graphics2D g2d = new Graphics2D();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that paintBody() method is called
        assertTrue(g2d.isPaintBodyCalled());
    }

    @Test
    public void test_Render_SelectedRobotIdsContainsId() {
        selectedRobotIds.add(robotSnapshot.getId());
        Graphics2D g2d = new Graphics2D();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that paintSelection() method is called
        assertTrue(g2d.isPaintSelectionCalled());
    }

    @Test
    public void test_Render_SelectedRobotIdsDoesNotContainId() {
        Graphics2D g2d = new Graphics2D();
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert that paintSelection() method is not called
        assertFalse(g2d.isPaintSelectionCalled());
    }

    @Test
    public void test_SetRenderDead_True() {
        robotRenderer.setRenderDead(true);
        
        assertTrue(robotRenderer.isRenderDead());
    }

    @Test
    public void test_SetRenderDead_False() {
        robotRenderer.setRenderDead(false);
        
        assertFalse(robotRenderer.isRenderDead());
    }

    @Test
    public void test_SetShowStatusBars_True() {
        robotRenderer.setShowStatusBars(true);
        
        assertTrue(robotRenderer.isShowStatusBars());
    }

    @Test
    public void test_SetShowStatusBars_False() {
        robotRenderer.setShowStatusBars(false);
        
        assertFalse(robotRenderer.isShowStatusBars());
    }

    @Test
    public void test_SetShowName_True() {
        robotRenderer.setShowName(true);
        
        assertTrue(robotRenderer.isShowName());
    }

    @Test
    public void test_SetShowName_False() {
        robotRenderer.setShowName(false);
        
        assertFalse(robotRenderer.isShowName());
    }

    @Test
    public void test_SetFillShield_True() {
        robotRenderer.setFillShield(true);
        
        assertTrue(robotRenderer.isFillShield());
    }

    @Test
    public void test_SetFillShield_False() {
        robotRenderer.setFillShield(false);
        
        assertFalse(robotRenderer.isFillShield());
    }
}