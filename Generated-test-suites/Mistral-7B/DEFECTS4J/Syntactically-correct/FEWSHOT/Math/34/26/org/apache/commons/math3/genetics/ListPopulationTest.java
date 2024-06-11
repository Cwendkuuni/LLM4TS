package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ListPopulationTest {

    @Test
    public void testNewInstanceWithPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testNewInstanceWithInitialChromosomes() {
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }

            @Override
            public String toString() {
                return "chromosome1";
            }
        };

        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }

            @Override
            public String toString() {
                return "chromosome2";
            }
        };

        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        assertEquals(10, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome1));
        assertTrue(population.getChromosomes().contains(chromosome2));
    }

    @Test(expected = NotPositiveException.class)
    public void testNewInstanceWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testSetChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);

        Chromosome chromosomeToAdd = new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }

            @Override
            public String toString() {
                return "chromosomeToAdd";
            }
        };

        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(chromosomeToAdd);

        population.setChromosomes(chromosomesToAdd);

        assertEquals(3, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosomeToAdd));
    }

    @Test(expected = NullArgumentException.class)
    public void testSetNullChromosomes() {
        ListPopulation population = new ListPopulation(Collections.<Chromosome> emptyList(), 10);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithLargeCollection() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);

        Chromosome chromosomeToAdd = new Chromosome() {
            @Override
            public double getFitness() {
                return 4.0;
            }

            @Override
            public String toString() {
                return "chromosomeToAdd";
            }
        };

        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            chromosomesToAdd.add(chromosomeToAdd);
        }

        population.setChromosomes(chromosomesToAdd);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(Collections.<Chromosome> emptyList(), 10);

        Chromosome chromosomeToAdd = new Chromosome() {
            @Override
            public double getFitness() {
                return 5.0;
            }

            @Override
            public String toString() {
                return "chromosomeToAdd";
            }
        };

        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(chromosomeToAdd);

        population.addChromosomes(chromosomesToAdd);

        assertEquals(1, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosomeToAdd));
    }

    @Test(expected = NullArgumentException.class)
    public void testAddNullChromosomes() {
        ListPopulation population = new ListPopulation(Collections.<Chromosome> emptyList(), 10);
        population.addChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithLargeCollection() {
        ListPopulation population = new ListPopulation(Collections.<Chromosome> emptyList(), 5);

        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Chromosome chromosomeToAdd = new Chromosome() {
                @Override
                public double getFitness() {
                    return new Random().nextDouble();
                }

                @Override
                public String toString() {
                    return "chromosomeToAdd" + i;
                }
            };
            chromosomesToAdd.add(chromosomeToAdd);
        }

        population.addChromosomes(chromosomesToAdd);
    }

    @Test
    public void testGetChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);

        Chromosome chromosomeToAdd = new Chromosome() {
            @Override
            public double getFitness() {
                return 6.0;
            }

            @Override
            public String toString() {
                return "chromosomeToAdd";
            }
        };

        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(chromosomeToAdd);
        population.addChromosomes(chromosomesToAdd);

        List<Chromosome> chromosomes = population.getChromosomes();

        assertEquals(3, population.getPopulationSize());
        assertTrue(chromosomes.contains(chromosomeToAdd));
        assertTrue(chromosomes.containsAll(initialChromosomes));
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);

        Chromosome chromosomeToAdd = new Chromosome() {
            @Override
            public double getFitness() {
                return 7.0;
            }

            @Override
            public String toString() {
                return "chromosomeToAdd";
            }
        };

        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(chromosomeToAdd);
        population.addChromosomes(chromosomesToAdd);

        List<Chromosome> chromosomeList = population.getChromosomeList();

        assertEquals(3, population.getPopulationSize());
        assertEquals(chromosomeList, population.getChromosomes());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(Collections.<Chromosome> emptyList(), 5);

        Chromosome chromosomeToAdd = new Chromosome() {
            @Override
            public double getFitness() {
                return 8.0;
            }

            @Override
            public String toString() {
                return "chromosomeToAdd";
            }
        };

        population.addChromosome(chromosomeToAdd);

        assertEquals(1, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosomeToAdd));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithFullPopulation() {
        ListPopulation population = new ListPopulation(Collections.<Chromosome> emptyList(), 5);

        Chromosome chromosomeToAdd = new Chromosome() {
            @Override
            public double getFitness() {
                return 9.0;
            }

            @Override
            public String toString() {
                return "chromosomeToAdd";
            }
        };

        population.addChromosome(chromosomeToAdd);
    }

    @Test
    public void testGetFittestChromosome() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);

        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 11.0;
            }

            @Override
            public String toString() {
                return "chromosome1";
            }
        };

        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 12.0;
            }

            @Override
            public String toString() {
                return "chromosome2";
            }
        };

        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(chromosome1);
        chromosomesToAdd.add(chromosome2);
        population.addChromosomes(chromosomesToAdd);

        Chromosome fittestChromosome = population.getFittestChromosome();

        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome2, fittestChromosome);
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(Collections.<Chromosome> emptyList(), 10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitNegative() {
        ListPopulation population = new ListPopulation(Collections.<Chromosome> emptyList(), 10);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitSmallerCurrentSize() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        population.addChromosomes(initialChromosomes);
        population.setPopulationLimit(5);
    }

    @Test
    public void testSetPopulationLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        population.addChromosomes(initialChromosomes);
        population.setPopulationLimit(20);

        assertEquals(10, population.getPopulationSize());
        assertEquals(20, population.getPopulationLimit());
    }

    @Test
    public void testGetPopulationSize() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        population.addChromosomes(initialChromosomes);

        assertEquals(10, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);

        population.addChromosomes(initialChromosomes);

        assertEquals("[chromosome1, chromosome2, ..., chromosomeN]", population.toString());
    }

    @Test
    public void testIterator() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        population.addChromosomes(initialChromosomes);

        Iterator<Chromosome> iterator = population.iterator();

        assertTrue(iterator.hasNext());
        Chromosome chromosome = iterator.next();
        assertEquals(initialChromosomes.get(0), chromosome);

        assertTrue(iterator.hasNext());
        chromosome = iterator.next();
        assertEquals(initialChromosomes.get(1), chromosome);

        assertTrue(iterator.hasNext());
        // ...

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testRemoveFromIteratorThrowsException() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        ListPopulation population = new ListPopulation(initialChromosomes, 10);
        population.addChromosomes(initialChromosomes);

        Iterator<Chromosome> iterator = population.iterator();

        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }
}