package net.virtualinfinity.atrobots.gui.renderers;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class RobotRendererTest {
    private RobotRenderer robotRenderer;

    @Before
    public void setUp() {
        robotRenderer = new RobotRenderer();
    }

    @Test
    public void testRender_whenRobotIsDeadAndRenderDeadIsTrue_thenSkipRendering() {
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(true);

        robotRenderer.render(g2d, robotSnapshot, new HashSet<>());

        assertTrue(((Graphics2DStub) g2d).isCleared());
    }

    @Test
    public void testRender_whenRobotIsDeadAndRenderDeadIsFalse_thenRenderSelectionAndStatusBars() {
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(true);
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());

        robotRenderer.setRenderDead(false);
        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        assertTrue(((Graphics2DStub) g2d).isFilled());
    }

    @Test
    public void testRender_whenRobotIsNotDead_thenRenderBodyShieldTurretNameStatusBarsAndSelection() {
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setDead(false);
        robotSnapshot.setName("Robot");
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        assertTrue(((Graphics2DStub) g2d).isFilled());
        assertTrue(((Graphics2DStub) g2d).isStroked());
        assertTrue(((Graphics2DStub) g2d).isDrawn());
        assertTrue(((Graphics2DStub) g2d).isPainted());
    }

    @Test
    public void testRender_whenRobotIsSelected_thenRenderSelection() {
        Graphics2D g2d = new Graphics2DStub();
        RobotSnapshot robotSnapshot = new RobotSnapshot();
        Set<Integer> selectedRobotIds = new HashSet<>();
        selectedRobotIds.add(robotSnapshot.getId());

        robotRenderer.render(g2d, robotSnapshot, selectedRobotIds);

        assertTrue(((Graphics2DStub) g2d).isFilled());
    }

    private static class Graphics2DStub extends Graphics2D {
        private boolean isCleared;
        private boolean isFilled;
        private boolean isStroked;
        private boolean isDrawn;
        private boolean isPainted;

        public boolean isCleared() {
            return isCleared;
        }

        public boolean isFilled() {
            return isFilled;
        }

        public boolean isStroked() {
            return isStroked;
        }

        public boolean isDrawn() {
            return isDrawn;
        }

        public boolean isPainted() {
            return isPainted;
        }

        @Override
        public void clearRect(int x, int y, int width, int height) {
            isCleared = true;
        }

        @Override
        public void fill(Shape s) {
            isFilled = true;
        }

        @Override
        public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
            return false;
        }

        @Override
        public void draw(Shape s) {
            isDrawn = true;
        }

        @Override
        public void drawString(String str, int x, int y) {
            isDrawn = true;
        }

        @Override
        public void drawString(String str, float x, float y) {
            isDrawn = true;
        }

        @Override
        public void translate(int x, int y) {

        }

        @Override
        public void translate(double tx, double ty) {

        }

        @Override
        public void rotate(double theta) {

        }

        @Override
        public void rotate(double theta, double x, double y) {

        }

        @Override
        public void scale(double sx, double sy) {

        }

        @Override
        public void shear(double shx, double shy) {

        }

        @Override
        public void transform(AffineTransform Tx) {

        }

        @Override
        public void setTransform(AffineTransform Tx) {

        }

        @Override
        public AffineTransform getTransform() {
            return null;
        }

        @Override
        public Paint getPaint() {
            return null;
        }

        @Override
        public Color getColor() {
            return null;
        }

        @Override
        public void setColor(Color c) {

        }

        @Override
        public void setPaint(Paint paint) {
            isPainted = true;
        }

        @Override
        public void setStroke(Stroke s) {
            isStroked = true;
        }

        @Override
        public Stroke getStroke() {
            return null;
        }

        @Override
        public void clip(Shape s) {

        }

        @Override
        public FontRenderContext getFontRenderContext() {
            return null;
        }

        @Override
        public Graphics create() {
            return null;
        }

        @Override
        public void dispose() {

        }

        // Other abstract methods are left unimplemented
    }
}