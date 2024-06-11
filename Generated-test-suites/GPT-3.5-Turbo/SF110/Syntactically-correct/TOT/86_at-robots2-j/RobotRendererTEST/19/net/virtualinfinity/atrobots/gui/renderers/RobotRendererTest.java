package net.virtualinfinity.atrobots.gui.renderers.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;

public class RobotRendererTest {

    @Test
    public void testRender_RobotIsDeadAndRenderDeadIsTrue_ShouldNotRender() {
        // Arrange
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(true);
        Mockito.when(robotSnapshot.isDead()).thenReturn(true);
        
        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());
        
        // Assert
        Mockito.verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_RobotIsDeadAndRenderDeadIsFalse_ShouldRender() {
        // Arrange
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setRenderDead(false);
        Mockito.when(robotSnapshot.isDead()).thenReturn(true);
        
        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());
        
        // Assert
        Mockito.verify(g2d, Mockito.times(2)).draw(Mockito.any(Shape.class));
    }
    
    @Test
    public void testRender_ShouldRenderBody() {
        // Arrange
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        
        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());
        
        // Assert
        Mockito.verify(g2d).fill(Mockito.any(Shape.class));
    }
    
    @Test
    public void testRender_ActiveShieldIsFalse_ShouldNotRenderShield() {
        // Arrange
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        Mockito.when(robotSnapshot.isActiveShield()).thenReturn(false);
        
        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());
        
        // Assert
        Mockito.verifyZeroInteractions(g2d);
    }
    
    @Test
    public void testRender_IsFillShieldIsTrue_ShouldFillShield() {
        // Arrange
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setFillShield(true);
        Mockito.when(robotSnapshot.isActiveShield()).thenReturn(true);
        
        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());
        
        // Assert
        Mockito.verify(g2d, Mockito.times(4)).fill(Mockito.any(Shape.class));
    }
    
    @Test
    public void testRender_IsShowNameIsTrue_ShouldDrawName() {
        // Arrange
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        robotRenderer.setShowName(true);
        
        // Act
        robotRenderer.render(g2d, robotSnapshot, Collections.emptySet());
        
        // Assert
        Mockito.verify(g2d).drawString(Mockito.anyString(), Mockito.anyFloat(), Mockito.anyFloat());
    }
    
    @Test
    public void testRender_SelectedRobotIdsContainsCurrentRobotId_ShouldPaintSelection() {
        // Arrange
        Graphics2D g2d = Mockito.mock(Graphics2D.class);
        RobotSnapshot robotSnapshot = Mockito.mock(RobotSnapshot.class);
        
        RobotRenderer robotRenderer = new RobotRenderer();
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());
        
        // Act
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // Assert
        Mockito.verify(g2d).fill(Mockito.any(Shape.class));
    }
    
    @Test
    public void testSetRenderDead_SetToFalse_ShouldBeFalse() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();
        
        // Act
        robotRenderer.setRenderDead(false);
        
        // Assert
        assertFalse(robotRenderer.isRenderDead());
    }
    
    @Test
    public void testSetShowStatusBars_SetToFalse_ShouldBeFalse() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();
        
        // Act
        robotRenderer.setShowStatusBars(false);
        
        // Assert
        assertFalse(robotRenderer.isShowStatusBars());
    }
    
    @Test
    public void testSetShowName_SetToFalse_ShouldBeFalse() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();
        
        // Act
        robotRenderer.setShowName(false);
        
        // Assert
        assertFalse(robotRenderer.isShowName());
    }
    
    @Test
    public void testSetFillShield_SetToFalse_ShouldBeFalse() {
        // Arrange
        RobotRenderer robotRenderer = new RobotRenderer();
        
        // Act
        robotRenderer.setFillShield(false);
        
        // Assert
        assertFalse(robotRenderer.isFillShield());
    }
    
}