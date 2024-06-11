package org.apache.commons.math3.genetics;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.ListPopulation;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for {@link ListPopulation}.
 */
public class ListPopulationTest {

    private ListPopulation population;

    @Before
    public void setup() {
        population = new ListPopulation(2);
    }

    /**
     * Test constructor when population limit is positive.
     */
    @Test
    public void testConstructorPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    /**
     * Test constructor when population limit is not positive.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    /**
     * Test method getChromosomes().
     */
    @Test
    public void testGetChromosomes() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 0.0;
            }

            @Override
            public String toString() {
                return "initial chromosome";
            }
        }, new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "second chromosome";
            }
        });
        population = new ListPopulation(initialChromosomes);
        List<Chromosome> expectedChromosomes = Collections.unmodifiableList(initialChromosomes);
        List<Chromosome> actualChromosomes = population.getChromosomes();
        Assert.assertEquals(expectedChromosomes, actualChromosomes);
    }

    /**
     * Test method addChromosomes().
     */
    @Test
    public void testAddChromosomes() {
        population.addChromosomes(Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "third chromosome";
            }
        }));
        List<Chromosome> expectedChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 0.0;
            }

            @Override
            public String toString() {
                return "initial chromosome";
            }
        }, new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "second chromosome";
            }
        }, new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "third chromosome";
            }
        });
        List<Chromosome> actualChromosomes = population.getChromosomes();
        Assert.assertEquals(expectedChromosomes, actualChromosomes);
    }

    /**
     * Test method setChromosomes().
     */
    @Test
    public void testSetChromosomes() {
        List<Chromosome> initialChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 0.0;
            }

            @Override
            public String toString() {
                return "initial chromosome";
            }
        }, new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "second chromosome";
            }
        });
        population = new ListPopulation(2);
        population.setChromosomes(initialChromosomes);

        List<Chromosome> expectedChromosomes = Collections.unmodifiableList(initialChromosomes);
        List<Chromosome> actualChromosomes = population.getChromosomes();
        Assert.assertEquals(expectedChromosomes, actualChromosomes);
    }

    /**
     * Test method setPopulationLimit() when new limit is greater than current size.
     */
    @Test
    public void testSetPopulationLimitGreaterThanCurrentSize() {
        population.addChromosomes(Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 0.0;
            }

            @Override
            public String toString() {
                return "initial chromosome";
            }
        }));
        population.setPopulationLimit(5);
        List<Chromosome> expectedChromosomes = Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 0.0;
            }

            @Override
            public String toString() {
                return "initial chromosome";
            }
        }, new Chromosome() {
            @Override
            public double getFitness() {
                return 0.0;
            }

            @Override
            public String toString() {
                return "second chromosome";
            }
        });
        List<Chromosome> actualChromosomes = population.getChromosomes();
        Assert.assertEquals(expectedChromosomes, actualChromosomes);
    }

    /**
     * Test method setPopulationLimit() when new limit is smaller than current size.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerThanCurrentSize() {
        population.addChromosomes(Arrays.asList(new Chromosome() {
            @Override
            public double getFitness() {
                return 0.0;
            }

            @Override
            public String toString() {
                return "initial chromosome";
            }
        }));
        population.setPopulationLimit(1);
    }
}