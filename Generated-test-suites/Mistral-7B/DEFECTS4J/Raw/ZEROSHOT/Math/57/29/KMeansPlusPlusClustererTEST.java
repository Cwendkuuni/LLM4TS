import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KMeansPlusPlusClustererTest {

private List<Clusterable<Double>> dataSet;
private KMeansPlusPlusClusterer<Clusterable<Double>> clustering;

@Before
public void setup() {
dataSet = Arrays.asList(new Point(0.0, 0.0), new Point(1.0, 0.0), new Point(1.5, 0.5), new Point(2.0, 0.0),
new Point(2.5, 1.0), new Point(3.0, 0.5), new Point(3.5, 1.5), new Point(4.0, 1.0));
clustering = new KMeansPlusPlusClusterer<>(new Random());
}

@Test
public void testCluster() {
List<Cluster<Clusterable<Double>>> result = clustering.cluster(dataSet, 3, 10);
assertEquals(3, result.size());
for (Cluster<Clusterable<Double>> cluster : result) {
assertEquals(3, cluster.getPoints().size());
assertNotNull(cluster.getCenter());
}
}

@Test
public void testEmptyClusterStrategyLargestVariance() {
dataSet.add(new Point(-1.0, -1.0));
dataSet.add(new Point(-1.0, 1.0));
dataSet.add(new Point(1.0, 1.0));
dataSet.add(new Point(1.0, -1.0));

List<Cluster<Clusterable<Double>>> result = clustering.cluster(dataSet, 3, 10);
assertEquals(3, result.size());

Cluster<Clusterable<Double>> emptyCluster = result.get(2);
assertEquals(1, emptyCluster.getPoints().size());
Cluster<Clusterable<Double>> nonEmptyCluster = result.get(0);
assertEquals(3, nonEmptyCluster.getPoints().size());

Clusterable<Double> farPoint = new Point(4.0, 2.0);
Cluster<Clusterable<Double>> newCluster = clustering.getCluster(dataSet, Collections.singletonList(farPoint), EmptyClusterStrategy.LARGEST_VARIANCE);
assertEquals(1, newCluster.getPoints().size());
assertEquals(farPoint, newCluster.getCenter());
}

@Test(expected = ConvergenceException.class)
public void testEmptyClusterStrategyError() {
dataSet.add(new Point(-1.0, -1.0));
dataSet.add(new Point(-1.0, 1.0));
dataSet.add(new Point(1.0, 1.0));
dataSet.add(new Point(1.0, -1.0));

List<Cluster<Clusterable<Double>>> result = clustering.cluster(dataSet, 3, 10);
assertEquals(3, result.size());

Cluster<Clusterable<Double>> emptyCluster = result.get(2);
assertEquals(1, emptyCluster.getPoints().size());

clustering.setEmptyStrategy(EmptyClusterStrategy.ERROR);
List<Cluster<Clusterable<Double>>> newResult = clustering.cluster(dataSet, 3, 10);
assertEquals(3, newResult.size());
emptyCluster = newResult.get(2);
assertEquals(0, emptyCluster.getPoints().size());
}
}

class Point implements Clusterable<Point> {

private double x;
private double y;

public Point(double x, double y) {
this.x = x;
this.y = y;
}

@Override
public double distanceFrom(Clusterable<?> other) {
Clusterable<Point> otherPoint = (Clusterable<Point>) other;
double dx = Math.abs(x - otherPoint.getX());
double dy = Math.abs(y - otherPoint.getY());
return Math.sqrt(dx * dx + dy * dy);
}

@Override
public double getX() {
return x;
}

@Override
public double getY() {
return y;
}
}