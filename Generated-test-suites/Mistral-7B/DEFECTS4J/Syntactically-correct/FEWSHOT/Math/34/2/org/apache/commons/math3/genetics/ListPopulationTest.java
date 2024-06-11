package org.apache.commons.math3.genetics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.Population;

public class ListPopulationTest {

    @Test
    public void testListPopulationWithEmptyList() {
        ListPopulation population = new ListPopulation(10);
        assertEquals(0, population.getPopulationSize());
        assertNotNull(population.getChromosomes());
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testListPopulationWithNullList() {
        try {
            new ListPopulation(null, 10);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationWithPopulationLimitZero() {
        try {
            new ListPopulation(Collections.emptyList(), 0);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationWithPopulationLimitNegative() {
        try {
            new ListPopulation(Collections.emptyList(), -1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationWithPopulationLimitTooSmall() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        });
        try {
            new ListPopulation(chromosomes, 0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationWithPopulationLimitAndEmptyList() {
        ListPopulation population = new ListPopulation(Collections.emptyList(), 10);
        assertEquals(0, population.getPopulationSize());
        assertNotNull(population.getChromosomes());
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testListPopulationWithPopulationLimitAndChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        chromosomes.add(chromosome1);
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        };
        chromosomes.add(chromosome2);
        ListPopulation population = new ListPopulation(chromosomes, 5);
        assertEquals(5, population.getPopulationSize());
        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertNotNull(populationChromosomes);
        assertEquals(5, populationChromosomes.size());
        Iterator<Chromosome> iterator = populationChromosomes.iterator();
        Chromosome currentChromosome = iterator.next();
        assertEquals(chromosome1, currentChromosome);
        currentChromosome = iterator.next();
        assertEquals(chromosome2, currentChromosome);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testListPopulationWithPopulationLimitAndMoreChromosomesThanLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        };
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        });
        try {
            new ListPopulation(chromosomes, 2);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationSetChromosomesWithNullList() {
        try {
            ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
            population.setChromosomes(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationSetChromosomesWithEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        population.setChromosomes(Collections.emptyList());
        assertEquals(0, population.getPopulationSize());
        assertNotNull(population.getChromosomes());
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testListPopulationSetChromosomesWithMoreChromosomesThanLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        chromosomes.add(chromosome1);
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        };
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        });
        try {
            population.setChromosomes(chromosomes);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationSetChromosomesWithSameChromosomesAsBefore() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        chromosomes.add(chromosome1);
        population.setChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertEquals(1, populationChromosomes.size());
        Chromosome currentChromosome = populationChromosomes.get(0);
        assertEquals(chromosome1, currentChromosome);
    }

    @Test
    public void testListPopulationAddChromosomesWithNullCollection() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        try {
            population.addChromosomes(null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationAddChromosomesWithEmptyCollection() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        population.addChromosomes(Collections.emptyList());
        assertEquals(0, population.getPopulationSize());
        assertNotNull(population.getChromosomes());
        assertTrue(population.getChromosomes().isEmpty());
    }

    @Test
    public void testListPopulationAddChromosomesWithMoreChromosomesThanLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        };
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(new Chromosome() {
            @Override
            public double getFitness() {
                return 3.0;
            }
        });
        try {
            population.addChromosomes(chromosomes);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationAddChromosomesWithSameChromosomesAsBefore() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        chromosomes.add(chromosome1);
        population.addChromosomes(chromosomes);
        assertEquals(1, population.getPopulationSize());
        List<Chromosome> populationChromosomes = population.getChromosomes();
        assertEquals(1, populationChromosomes.size());
        Chromosome currentChromosome = populationChromosomes.get(0);
        assertEquals(chromosome1, currentChromosome);
    }

    @Test
    public void testListPopulationGetFittestChromosomeWithEmptyList() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        assertNull(population.getFittestChromosome());
    }

    @Test
    public void testListPopulationGetFittestChromosomeWithOneChromosome() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>() {{
            add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 1.0;
                }
            });
        }});
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertNotNull(fittestChromosome);
        assertEquals(1.0, fittestChromosome.getFitness(), 0.0);
    }

    @Test
    public void testListPopulationGetFittestChromosomeWithMultipleChromosomes() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>() {{
            add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 1.0;
                }
            });
            add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 2.0;
                }
            });
        }});
        Chromosome fittestChromosome = population.getFittestChromosome();
        assertNotNull(fittestChromosome);
        assertEquals(2.0, fittestChromosome.getFitness(), 0.0);
    }

    @Test
    public void testListPopulationGetPopulationLimit() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimitWithNegativeValue() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        try {
            population.setPopulationLimit(-1);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationSetPopulationLimitWithSameValueAsBefore() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        population.setPopulationLimit(10);
        assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationSetPopulationLimitWithValueSmallerThanCurrentSize() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>(10));
        population.setPopulationLimit(5);
        try {
            population.setPopulationLimit(5);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testListPopulationToString() {
        ListPopulation population = new ListPopulation(new ArrayList<Chromosome>() {{
            add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 1.0;
                }
            });
            add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 2.0;
                }
            });
        }});
        assertEquals("[Chromosome@7128, Chromosome@196b74]", population.toString());
    }
}