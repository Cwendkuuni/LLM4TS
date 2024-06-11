import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    private final double EPSILON = 1e-10;
    
    @Test
    public void testDoOptimize_edgeCases() {
        //Given
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        
        //When
        double[] startPoint = {1.0, 2.0, 3.0};
        double[] target = {1.0, 2.0, 3.0};
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setConvergenceChecker(checker);
        PointVectorValuePair result = optimizer.doOptimize();
        
        //Then
        double[] expectedPoint = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedPoint, result.getPoint(), EPSILON);
    }
    
    @Test
    public void testDoOptimize_typicalUseCase() {
        //Given
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        
        //When
        double[] startPoint = {0.0, 0.0, 0.0};
        double[] target = {1.0, 2.0, 3.0};
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setConvergenceChecker(checker);
        PointVectorValuePair result = optimizer.doOptimize();
        
        //Then
        double[] expectedPoint = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedPoint, result.getPoint(), EPSILON);
    }
    
    @Test
    public void testDoOptimize_errorScenario() {
        //Given
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        
        //When
        double[] startPoint = null;
        double[] target = {1.0, 2.0, 3.0};
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker();
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(target);
        optimizer.setConvergenceChecker(checker);
        
        //Then
        assertThrows(NullArgumentException.class, optimizer::doOptimize);
    }
}