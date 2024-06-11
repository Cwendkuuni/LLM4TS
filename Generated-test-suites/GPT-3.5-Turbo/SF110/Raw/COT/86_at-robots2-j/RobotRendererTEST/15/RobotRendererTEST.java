
import org.junit.Test;
import static org.junit.Assert.*;

public class RobotRendererTest {

    @Test
    public void testIsRenderDead() {
        RobotRenderer renderer = new RobotRenderer();

        boolean renderDead = renderer.isRenderDead();
        
        assertTrue(renderDead);
    }
    
    @Test
    public void testSetRenderDead() {
        RobotRenderer renderer = new RobotRenderer();

        renderer.setRenderDead(false);
        boolean renderDead = renderer.isRenderDead();
        
        assertFalse(renderDead);
    }
    
    @Test
    public void testIsShowStatusBars() {
        RobotRenderer renderer = new RobotRenderer();

        boolean showStatusBars = renderer.isShowStatusBars();
        
        assertTrue(showStatusBars);
    }
    
    @Test
    public void testSetShowStatusBars() {
        RobotRenderer renderer = new RobotRenderer();

        renderer.setShowStatusBars(false);
        boolean showStatusBars = renderer.isShowStatusBars();
        
        assertFalse(showStatusBars);
    }
    
    @Test
    public void testIsShowName() {
        RobotRenderer renderer = new RobotRenderer();

        boolean showName = renderer.isShowName();
        
        assertTrue(showName);
    }
    
    @Test
    public void testSetShowName() {
        RobotRenderer renderer = new RobotRenderer();

        renderer.setShowName(false);
        boolean showName = renderer.isShowName();
        
        assertFalse(showName);
    }
    
    @Test
    public void testIsFillShield() {
        RobotRenderer renderer = new RobotRenderer();

        boolean fillShield = renderer.isFillShield();
        
        assertTrue(fillShield);
    }
    
    @Test
    public void testSetFillShield() {
        RobotRenderer renderer = new RobotRenderer();

        renderer.setFillShield(false);
        boolean fillShield = renderer.isFillShield();
        
        assertFalse(fillShield);
    }
    
    
}
