

package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

public class RobotRendererTest {

    @Mock
    private Graphics2D g2d;
    
    @Mock
    private RobotSnapshot robotSnapshot;
    
    private Set<Integer> selectedRobotIds;
    
    private RobotRenderer robotRenderer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(1);
        
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRender() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getId()).thenReturn(1);
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // verify that the expected methods were called with the correct arguments
        verify(g2d).setPaint(Color.red);
        verify(robotSnapshot, times(2)).getHeading();
        verify(robotSnapshot, times(2)).getX();
        verify(robotSnapshot, times(2)).getY();
        verify(robotSnapshot).getTurretHeading();
        verify(robotSnapshot).getArmor();
        verify(robotSnapshot).getTemperature();
        verify(robotSnapshot).getName();
        verify(g2d).fill(any(Rectangle2D.Double.class));
        verify(g2d).fill(any(Ellipse2D.Double.class));
        verify(g2d).draw(any(Line2D.Double.class));
        verify(g2d).setPaint(Color.yellow);
        verify(g2d).draw(anyString(), anyFloat(), anyFloat());
        verify(g2d).setPaint(Color.white);
        verify(g2d).draw(any(Line2D.Double.class));
    }
    
    @Test
    public void testRender_WithDeadRobot() {
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.getId()).thenReturn(1);
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // verify that the expected methods were called with the correct arguments
        verify(g2d).setPaint(Color.orange.darker().darker());
        verify(robotSnapshot, times(2)).getHeading();
        verify(robotSnapshot, times(2)).getX();
        verify(robotSnapshot, times(2)).getY();
        verify(robotSnapshot).getTurretHeading();
        verify(robotSnapshot).getArmor();
        verify(robotSnapshot).getTemperature();
        verify(robotSnapshot).getName();
        verify(g2d).fill(any(Rectangle2D.Double.class));
        verify(g2d, times(2)).draw(any(GeneralPath.class));
        verify(g2d).setStroke(any(BasicStroke.class));
        verify(g2d).setStroke(any(Stroke.class));
    }
    
    @Test
    public void testRender_WithSelectedRobot() {
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getId()).thenReturn(1);
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        
        selectedRobotIds.clear();
        selectedRobotIds.add(1);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // verify that the expected methods were called with the correct arguments
        verify(g2d).setPaint(new Color(1.0f, 1.0f, 0.0f, 0.25f));
        verify(robotSnapshot, times(2)).getX();
        verify(robotSnapshot, times(2)).getY();
        verify(g2d).fill(any(Ellipse2D.Double.class));
    }
    
    @Test
    public void testRender_RenderDeadFalse() {
        robotRenderer.setRenderDead(false);
        
        when(robotSnapshot.isDead()).thenReturn(true);
        when(robotSnapshot.getId()).thenReturn(1);
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // verify that the expected methods were not called
        verifyZeroInteractions(g2d);
        verifyZeroInteractions(robotSnapshot);
    }
    
    @Test
    public void testRender_WithoutStatusBars() {
        robotRenderer.setShowStatusBars(false);
        
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getId()).thenReturn(1);
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // verify that the expected methods were not called
        verify(g2d).setPaint(Color.red);
        verify(robotSnapshot, times(2)).getHeading();
        verify(robotSnapshot, times(2)).getX();
        verify(robotSnapshot, times(2)).getY();
        verify(robotSnapshot).isDead();
        verify(robotSnapshot).getName();
        verify(g2d).setPaint(Color.yellow);
        verify(g2d).drawString(anyString(), anyFloat(), anyFloat());
    }
    
    @Test
    public void testRender_WithoutName() {
        robotRenderer.setShowName(false);
        
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.getId()).thenReturn(1);
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // verify that the expected methods were not called
        verify(g2d).setPaint(Color.red);
        verify(robotSnapshot, times(2)).getHeading();
        verify(robotSnapshot, times(2)).getX();
        verify(robotSnapshot, times(2)).getY();
        verify(robotSnapshot).getTurretHeading();
        verify(robotSnapshot).getArmor();
        verify(robotSnapshot).getTemperature();
        verify(robotSnapshot).isDead();
        verify(g2d).setPaint(Color.white);
        verify(g2d).draw(any(Line2D.Double.class));
        verify(g2d).fill(any(Ellipse2D.Double.class));
        verify(g2d).fill(any(Rectangle2D.Double.class));
    }
    
    @Test
    public void testRender_WithoutFillShield() {
        robotRenderer.setFillShield(false);
        
        when(robotSnapshot.isDead()).thenReturn(false);
        when(robotSnapshot.isActiveShield()).thenReturn(true);
        when(robotSnapshot.getId()).thenReturn(1);
        when(robotSnapshot.getX()).thenReturn(0.0);
        when(robotSnapshot.getY()).thenReturn(0.0);
        
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);
        
        // verify that the expected methods were called with the correct arguments
        verify(g2d).setPaint(new Color(0.8f, 0.6f, 1.0f));
        verify(robotSnapshot, times(2)).getX();
        verify(robotSnapshot, times(2)).getY();
        verify(g2d).draw(any(Ellipse2D.Double.class));
        
        // verify that the not expected methods were not called
        verify(g2d, never()).fill(any(Ellipse2D.Double.class));
    }
}

