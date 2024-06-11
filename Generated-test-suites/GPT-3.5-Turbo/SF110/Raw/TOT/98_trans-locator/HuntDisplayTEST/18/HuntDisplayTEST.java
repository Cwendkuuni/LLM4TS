import org.junit.Test;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        // typical use case
        // create a TrigPoint object
        // call addPoint method with the created TrigPoint object
        // assert that the point is added to the points vector
        // assert that repaint method is called

        // edge cases
        // create a TrigPoint object with maximum Y value (90 degrees)
        // call addPoint method with the created TrigPoint object
        // assert that the point is added to the points vector
        // assert that repaint method is called

        // create a TrigPoint object with minimum X value (-180 degrees)
        // call addPoint method with the created TrigPoint object
        // assert that the point is added to the points vector
        // assert that repaint method is called

        // error scenarios
        // create a null TrigPoint object
        // call addPoint method with the null TrigPoint object
        // assert that no point is added to the points vector
        // assert that repaint method is not called
    }

    @Test
    public void testAddLandMark() {
        // typical use case
        // create a Landmark object
        // call addLandMark method with the created Landmark object
        // assert that the landmark is added to the landmarks vector
        // assert that repaint method is called

        // edge cases
        // create a Landmark object with maximum latitude value (90 degrees)
        // call addLandMark method with the created Landmark object
        // assert that the landmark is added to the landmarks vector
        // assert that repaint method is called

        // create a Landmark object with minimum longitude value (-180 degrees)
        // call addLandMark method with the created Landmark object
        // assert that the landmark is added to the landmarks vector
        // assert that repaint method is called
        
        // error scenarios
        // create a null Landmark object
        // call addLandMark method with the null Landmark object
        // assert that no landmark is added to the landmarks vector
        // assert that repaint method is not called
    }

    @Test
    public void testPaintComponent() {
        // typical use case
        // create a HuntDisplay object
        // set the necessary variables (points, landmarks, etc)
        // call paintComponent method
        // assert that the expected graphics elements are drawn correctly

        // edge cases
        // create a HuntDisplay object with no points and no landmarks
        // call paintComponent method
        // assert that nothing is drawn on the graphics
        
        // create a HuntDisplay object with multiple points and landmarks
        // call paintComponent method
        // assert that all points and landmarks are drawn correctly on the graphics

        // error scenarios
        // create a HuntDisplay object with a null graphics object
        // call paintComponent method
        // assert that no exception is thrown and no graphics is drawn
    }

    // Add more test cases for other methods if needed

}