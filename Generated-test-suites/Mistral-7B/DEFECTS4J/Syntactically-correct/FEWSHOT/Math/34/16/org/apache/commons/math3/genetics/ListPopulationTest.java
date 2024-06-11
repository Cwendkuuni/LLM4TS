package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ListPopulation}.
 */
public class ListPopulationTest {

    /** The population limit. */
    private int populationLimit;

    /** The chromosomes list. */
    private List<Chromosome> chromosomes;

    /** The list population. */
    private ListPopulation population;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setUp() {
        populationLimit = 5;
        chromosomes = new ArrayList<Chromosome>(populationLimit);
        population = new ListPopulation<Chromosome>(chromosomes, populationLimit);
    }

    /**
     * Tests the constructor with a null list of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChromosomesList() {
        new ListPopulation<Chromosome>(null, populationLimit);
    }

    /**
     * Tests the constructor with a negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorWithNegativePopulationLimit() {
        new ListPopulation<Chromosome>(chromosomes, -1);
    }

    /**
     * Tests the constructor with a list of chromosomes larger than the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructorWithChromosomesListBiggerThanPopulationLimit() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>(populationLimit + 1);
        new ListPopulation<Chromosome>(newChromosomes, populationLimit);
    }

    /**
     * Tests the setChromosomes method with a null list of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomesList() {
        population.setChromosomes(null);
    }

    /**
     * Tests the setChromosomes method with a list of chromosomes larger than the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithChromosomesListBiggerThanPopulationLimit() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>(populationLimit + 1);
        population.setChromosomes(newChromosomes);
    }

    /**
     * Tests the addChromosomes method with a null collection of chromosomes.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomesWithNullChromosomesCollection() {
        population.addChromosomes(null);
    }

    /**
     * Tests the addChromosomes method with a collection of chromosomes larger than the population limit.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithChromosomesCollectionBiggerThanPopulationLimit() {
        Collection<Chromosome> newChromosomes = new ArrayList<Chromosome>();
        newChromosomes.add(new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        });
        newChromosomes.add(new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        });
        population.addChromosomes(newChromosomes);
    }

    /**
     * Tests the getChromosomes method.
     */
    @Test
    public void testGetChromosomes() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>(2);
        population.addChromosomes(newChromosomes);
        List<Chromosome> result = population.getChromosomes();
        assertSame(newChromosomes, result);
    }

    /**
     * Tests the getChromosomeList method.
     */
    @Test
    public void testGetChromosomeList() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>(2);
        population.addChromosomes(newChromosomes);
        List<Chromosome> result = population.getChromosomeList();
        assertSame(newChromosomes, result);
    }

    /**
     * Tests the addChromosome method with a null chromosome.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddChromosomeWithNullChromosome() {
        population.addChromosome(null);
    }

    /**
     * Tests the addChromosome method with a population limit reached.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithPopulationLimitReached() {
        population.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        });
    }

    /**
     * Tests the getFittestChromosome method.
     */
    @Test
    public void testGetFittestChromosome() {
        Chromosome first = new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 1;
            }
        };
        Chromosome second = new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        };
        population.addChromosome(first);
        population.addChromosome(second);
        Chromosome result = population.getFittestChromosome();
        assertEquals(second, result);
    }

    /**
     * Tests the getPopulationLimit method.
     */
    @Test
    public void testGetPopulationLimit() {
        assertEquals(populationLimit, population.getPopulationLimit());
    }

    /**
     * Tests the setPopulationLimit method with a negative population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        population.setPopulationLimit(-1);
    }

    /**
     * Tests the setPopulationLimit method with a population limit smaller than the current number of
     * chromosomes.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithPopulationSizeBiggerThanNewPopulationLimit() {
        population.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        });
        population.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        });
        population.setPopulationLimit(1);
    }

    /**
     * Tests the getPopulationSize method.
     */
    @Test
    public void testGetPopulationSize() {
        assertEquals(0, population.getPopulationSize());
        population.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        });
        assertEquals(1, population.getPopulationSize());
        population.addChromosome(new Chromosome() {
            @Override
            public int compareTo(Chromosome obj) {
                return 0;
            }
        });
        assertEquals(2, population.getPopulationSize());
    }

    /**
     * Tests the toString method.
     */
    @Test
    public void testToString() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>(2);
        population.addChromosomes(newChromosomes);
        String result = population.toString();
        String expected = "[";
        for (Chromosome chromosome : newChromosomes) {
            expected += chromosome.toString() + ", ";
        }
        expected = expected.substring(0, expected.length() - 2) + "]";
        assertEquals(expected, result);
    }

    /**
     * Tests the iterator method.
     */
    @Test
    public void testIterator() {
        List<Chromosome> newChromosomes = new ArrayList<Chromosome>(2);
        population.addChromosomes(newChromosomes);
        Iterator<Chromosome> iterator = population.iterator();
        List<Chromosome> result = new ArrayList<Chromosome>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        assertSame(newChromosomes, result);
    }
}