import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.stat.clustering.Cluster;
import org.apache.commons.math3.stat.clustering.Clusterable;
import org.apache.commons.math3.stat.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansPlusPlusClustererTest {

    @Test
    public void testClusterEmptyCollection() {
        // Test case for empty collection
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Clusterable> points = new ArrayList<>();
        try {
            List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 3, 10);
            Assert.fail("Expected ConvergenceException for empty collection");
        } catch (ConvergenceException e) {
            // Expected exception
        }
    }

    @Test
    public void testClusterSinglePoint() {
        // Test case for single point
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Clusterable> points = List.of(new Clusterable() {
            @Override
            public double[] getCoordinates() {
                return new double[]{0.0, 0.0};
            }
        });
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 3, 10);
        Assert.assertEquals(1, clusters.size());
    }

    @Test
    public void testClusterTwoPoints() {
        // Test case for two points
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Clusterable> points = List.of(
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{0.0, 0.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{1.0, 1.0};
                    }
                }
        );
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 3, 10);
        Assert.assertEquals(2, clusters.size());
        Cluster<Clusterable> firstCluster = clusters.get(0);
        Cluster<Clusterable> secondCluster = clusters.get(1);
        Assert.assertEquals(1, firstCluster.getPoints().size());
        Assert.assertEquals(1, secondCluster.getPoints().size());
        Assert.assertNotEquals(firstCluster.getCenter(), secondCluster.getCenter());
    }

    @Test
    public void testClusterMultiplePoints() {
        // Test case for multiple points
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Clusterable> points = List.of(
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{0.0, 0.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{1.0, 1.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{2.0, 2.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{3.0, 3.0};
                    }
                }
        );
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 4, 10);
        Assert.assertEquals(4, clusters.size());
        for (Cluster<Clusterable> cluster : clusters) {
            Assert.assertNotEmpty(cluster.getPoints());
        }
        for (int i = 0; i < clusters.size(); i++) {
            Cluster<Clusterable> cluster = clusters.get(i);
            Cluster<Clusterable> expectedCluster = getExpectedCluster(i + 1, points);
            Assert.assertEquals(expectedCluster.getPoints().size(), cluster.getPoints().size());
            for (Clusterable point : expectedCluster.getPoints()) {
                Assert.assertTrue(cluster.getPoints().contains(point));
            }
            Assert.assertEquals(expectedCluster.getCenter().getCoordinates(), cluster.getCenter().getCoordinates(), 0.001);
        }
    }

    @Test
    public void testClusterWithEmptyStrategyLargestVariance() {
        // Test case for empty cluster with largest variance strategy
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
        List<Clusterable> points = List.of(
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{0.0, 0.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{1.0, 1.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{2.0, 2.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{3.0, 3.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{4.0, 4.0};
                    }
                }
        );
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 4, 10);
        Assert.assertEquals(4, clusters.size());
        Cluster<Clusterable> emptyCluster = clusters.get(0);
        Assert.assertEquals(0, emptyCluster.getPoints().size());
        Cluster<Clusterable> otherCluster = clusters.get(1);
        Assert.assertFalse(emptyCluster.equals(otherCluster));
    }

    @Test
    public void testClusterWithEmptyStrategyFarthestPoint() {
        // Test case for empty cluster with farthest point strategy
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.FARTHEST_POINT);
        List<Clusterable> points = List.of(
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{0.0, 0.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{1.0, 1.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{2.0, 2.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{3.0, 3.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{4.0, 4.0};
                    }
                }
        );
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 4, 10);
        Assert.assertEquals(4, clusters.size());
        Cluster<Clusterable> emptyCluster = clusters.get(0);
        Cluster<Clusterable> otherCluster = clusters.get(1);
        Assert.assertEquals(1, emptyCluster.getPoints().size());
        Clusterable farthestPoint = getFarthestPoint(points);
        Assert.assertEquals(farthestPoint, emptyCluster.getPoints().get(0));
        Assert.assertFalse(emptyCluster.equals(otherCluster));
    }

    @Test(expected = ConvergenceException.class)
    public void testClusterWithEmptyStrategyError() {
        // Test case for empty cluster with error strategy
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random(), KMeansPlusPlusClusterer.EmptyClusterStrategy.ERROR);
        List<Clusterable> points = List.of(
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{0.0, 0.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{1.0, 1.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{2.0, 2.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{3.0, 3.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{4.0, 4.0};
                    }
                }
        );
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 4, 10);
        Assert.assertEquals(4, clusters.size());
        if (clusters.get(0).getPoints().isEmpty()) {
            throw new ConvergenceException("Expected error for empty cluster");
        }
    }

    @Test
    public void testClusterWithMaxIterations() {
        // Test case for maximum iterations
        KMeansPlusPlusClusterer<Clusterable> clusterer = new KMeansPlusPlusClusterer<>(new Random());
        List<Clusterable> points = List.of(
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{0.0, 0.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{1.0, 1.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{2.0, 2.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{3.0, 3.0};
                    }
                },
                new Clusterable() {
                    @Override
                    public double[] getCoordinates() {
                        return new double[]{4.0, 4.0};
                    }
                }
        );
        List<Cluster<Clusterable>> clusters = clusterer.cluster(points, 4, 1);
        Assert.assertEquals(4, clusters.size());
    }

    private Cluster<Clusterable> getExpectedCluster(int index, List<Clusterable> points) {
        Random random = new Random();
        List<Clusterable> expectedPoints;
        if (index <= 2) {
            expectedPoints = new ArrayList<>(points.subList(0, index));
        } else {
            int firstIndex = random.nextInt(index - 1);
            int lastIndex = random.nextInt(points.size() - index + 1);
            expectedPoints = new ArrayList<>(points.subList(firstIndex, lastIndex));
        }
        return new Cluster<>(new Clusterable() {
            @Override
            public double[] getCoordinates() {
                double[] coordinates = new double[2];
                for (int i = 0; i < 2; i++) {
                    coordinates[i] = expectedPoints.get(0).getCoordinates()[i] + random.nextDouble() * 0.1;
                }
                return coordinates;
            }
        });
    }

    private Clusterable getFarthestPoint(List<Clusterable> points) {
        Clusterable farthestPoint = points.get(0);
        double maxDistance = farthestPoint.distanceFrom(points.get(0).getCenter());
        for (Clusterable point : points) {
            double distance = point.distanceFrom(points.get(0).getCenter());
            if (distance > maxDistance) {
                farthestPoint = point;
                maxDistance = distance;
            }
        }
        return farthestPoint;
    }

}