package org.apache.commons.math3.genetics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

/**
 * Tests for {@link ListPopulation}.
 */
public class ListPopulationTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        List<Chromosome> initialChromosomes = Collections.singletonList(new Chromosome() {
            @Override
            public int fitness() {
                return 1;
            }
        });
        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals("Size of population should be equal to population limit", 10, chromosomes.size());
        Chromosome populationFittest = population.getFittestChromosome();
        assertEquals("Fittest chromosome should have highest fitness", populationFittest, chromosomes.get(0));
    }

    /**
     * Test constructor with null initial chromosomes list.
     */
    @Test(expected = NullArgumentException.class)
    public void testConstructorNullInitialChromosomesList() {
        new ListPopulation(null, 10);
    }

    /**
     * Test constructor with invalid population limit.
     */
    @Test(expected = NotPositiveException.class)
    public void testConstructorNegativePopulationLimit() {
        List<Chromosome> initialChromosomes = Collections.singletonList(new Chromosome() {
            @Override
            public int fitness() {
                return 1;
            }
        });
        new ListPopulation(initialChromosomes, -1);
    }

    /**
     * Test setting chromosomes with valid arguments.
     */
    @Test
    public void testSettingChromosomesValidArguments() {
        List<Chromosome> initialChromosomes = Collections.singletonList(new Chromosome() {
            @Override
            public int fitness() {
                return 1;
            }
        });
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.setChromosomes(initialChromosomes);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals("Size of population should be equal to initial chromosomes size", initialChromosomes.size(), chromosomes.size());
        Chromosome populationFittest = population.getFittestChromosome();
        assertEquals("Fittest chromosome should be the same as initial chromosomes fittest chromosome", populationFittest, initialChromosomes.get(0));
    }

    /**
     * Test setting chromosomes with null list.
     */
    @Test(expected = NullArgumentException.class)
    public void testSettingChromosomesNullList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.setChromosomes(null);
    }

    /**
     * Test setting chromosomes with invalid population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testSettingChromosomesInvalidPopulationSize() {
        List<Chromosome> initialChromosomes = Collections.singletonList(new Chromosome() {
            @Override
            public int fitness() {
                return 1;
            }
        });
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.setChromosomes(initialChromosomes);
        population.addChromosome(new Chromosome() {
            @Override
            public int fitness() {
                return 2;
            }
        });
        population.setChromosomes(initialChromosomes);
    }

    /**
     * Test adding chromosomes with valid arguments.
     */
    @Test
    public void testAddingChromosomesValidArguments() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        Chromosome chromosome = new Chromosome() {
            @Override
            public int fitness() {
                return 1;
            }
        };
        population.addChromosome(chromosome);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertEquals("Size of population should be equal to one plus initial chromosomes size", 1 + 0, chromosomes.size());
        Chromosome populationFittest = population.getFittestChromosome();
        assertEquals("Fittest chromosome should be the added chromosome", populationFittest, chromosome);
    }

    /**
     * Test adding chromosomes with null chromosome.
     */
    @Test(expected = NullArgumentException.class)
    public void testAddingChromosomesNullChromosome() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        population.addChromosome(null);
    }

    /**
     * Test adding chromosomes with invalid population size.
     */
    @Test(expected = NumberIsTooLargeException.class)
    public void testAddingChromosomesInvalidPopulationSize() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        List<Chromosome> initialChromosomes = Collections.singletonList(new Chromosome() {
            @Override
            public int fitness() {
                return 1;
            }
        });
        population.addChromosomes(initialChromosomes);
        population.addChromosome(new Chromosome() {
            @Override
            public int fitness() {
                return 2;
            }
        });
        population.addChromosomes(initialChromosomes);
    }

    /**
     * Test getting unmodifiable list of chromosomes.
     */
    @Test
    public void testGettingUnmodifiableListOfChromosomes() {
        List<Chromosome> initialChromosomes = Collections.singletonList(new Chromosome() {
            @Override
            public int fitness() {
                return 1;
            }
        });
        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        List<Chromosome> chromosomes = population.getChromosomes();
        assertFalse("List should be unmodifiable", chromosomes instanceof List);
        try {
            chromosomes.add(new Chromosome() {
                @Override
                public int fitness() {
                    return 2;
                }
            });
            assertFalse("Adding chromosome to unmodifiable list should throw exception", true);
        } catch (UnsupportedOperationException expected) {
            // expected
        }

        Chromosome chromosome = new Chromosome() {
            @Override
            public int fitness() {
                return 2;
            }
        };
        population.addChromosome(chromosome);

        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertEquals("List size should be one plus initial chromosomes size", 1 + initialChromosomes.size(), populationChromosomes.size());
        assertEquals("Population chromosomes should be the same as initial chromosomes plus added chromosome", populationChromosomes, initialChromosomes);
    }

    /**
     * Test getting list of chromosomes.
     */
    @Test
    public void testGettingListOfChromosomes() {
        List<Chromosome> initialChromosomes = Collections.singletonList(new Chromosome() {
            @Override
            public int fitness() {
                return 1;
            }
        });
        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        List<Chromosome> chromosomes = population.getChromosomeList();
        assertEquals("List size should be equal to population size", population.getPopulationSize(), chromosomes.size());
        chromosomes.set(0, new Chromosome() {
            @Override
            public int fitness() {
                return 3;
            }
        });
        population.getFittestChromosome(); // update fittest chromosome
        Chromosome populationFittest = population.getFittestChromosome();
        assertNotEquals("Fittest chromosome should not be the modified chromosome", populationFittest, chromosomes.get(0));
    }
}