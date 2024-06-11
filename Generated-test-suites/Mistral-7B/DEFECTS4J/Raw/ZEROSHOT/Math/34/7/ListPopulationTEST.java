import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test
    public void testListPopulationWithInitialChromosomes() {
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };

        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.5;
            }
        };

        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(chromosome1);
        initialChromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(initialChromosomes, 5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());

        List<Chromosome> actualChromosomes = population.getChromosomes();
        List<Chromosome> expectedChromosomes = initialChromosomes;

        assertEquals(expectedChromosomes.size(), actualChromosomes.size());
        for (int i = 0; i < expectedChromosomes.size(); i++) {
            assertEquals(expectedChromosomes.get(i).getFitness(), actualChromosomes.get(i).getFitness());
        }
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationWithNullChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationWithTooManyInitialChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        initialChromosomes.add(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.5;
            }
        });
        initialChromosomes.add(new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 2.0;
            }
        });

        new ListPopulation(initialChromosomes, 5);
    }

    @Test
    public void testAddChromosomeToListPopulation() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };

        population.addChromosome(chromosome);
        assertEquals(6, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesToListPopulationWithOverflow() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            chromosomesToAdd.add(new Chromosome() {
                @Override
                public double[] getGenes() {
                    return new double[0];
                }

                @Override
                public double getFitness() {
                    return 1.0;
                }
            });
        }
        population.addChromosomes(chromosomesToAdd);
    }

    @Test
    public void testAddChromosomesToListPopulation() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            chromosomesToAdd.add(new Chromosome() {
                @Override
                public double[] getGenes() {
                    return new double[0];
                }

                @Override
                public double getFitness() {
                    return 1.0 + i;
                }
            });
        }
        population.addChromosomes(chromosomesToAdd);
        assertEquals(8, population.getPopulationSize());
    }

    @Test
    public void testGetFittestChromosomeFromListPopulation() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 2.0;
            }
        };

        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };

        Chromosome chromosome3 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 0.5;
            }
        };

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        population.addChromosome(chromosome3);

        Chromosome expectedFittestChromosome = chromosome1;
        Chromosome actualFittestChromosome = population.getFittestChromosome();

        assertEquals(expectedFittestChromosome, actualFittestChromosome);
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };

        population.addChromosome(chromosome);
        population.addChromosome(chromosome);

        String expectedString = "[Chromosome[fitness=1.0], Chromosome[fitness=1.0]]";
        String actualString = population.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 2.0;
            }
        };

        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenes() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };

        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        Iterator<Chromosome> iterator = population.iterator();
        Chromosome expectedChromosome = chromosome1;
        Chromosome actualChromosome = iterator.next();

        assertEquals(expectedChromosome, actualChromosome);
        assertTrue(iterator.hasNext());
        expectedChromosome = chromosome2;
        actualChromosome = iterator.next();

        assertEquals(expectedChromosome, actualChromosome);
        assertFalse(iterator.hasNext());
    }
}