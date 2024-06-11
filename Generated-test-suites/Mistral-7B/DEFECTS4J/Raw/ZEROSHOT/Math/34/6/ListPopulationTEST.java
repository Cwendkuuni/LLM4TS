import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithValidPopulationLimit() {
        ListPopulation population = new ListPopulation(4);
        assertEquals(4, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithInvalidPopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationConstructorWithValidListAndPopulationLimit() {
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 0.5;
            }
        };
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        ListPopulation population = new ListPopulation(chromosomes, 4);
        assertEquals(4, population.getPopulationLimit());
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullList() {
        new ListPopulation(null, 4);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome() {
                @Override
                public double[] getGenotype() {
                    return new double[0];
                }

                @Override
                public double getFitness() {
                    return i + 1.0;
                }
            });
        }
        new ListPopulation(chromosomes, 5);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(4);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 0.5;
            }
        };
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);

        population.setChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullList() {
        ListPopulation population = new ListPopulation(4);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(4);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome() {
                @Override
                public double[] getGenotype() {
                    return new double[0];
                }

                @Override
                public double getFitness() {
                    return i + 1.0;
                }
            });
        }
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(4);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 0.5;
            }
        };
        population.addChromosomes(chromosomes);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosomes, population.getChromosomes());

        Chromosome chromosome3 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 0.75;
            }
        };
        population.addChromosome(chromosome3);
        assertEquals(3, population.getPopulationSize());
        assertTrue(population.getChromosomes().contains(chromosome3));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(4);
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chromosomes.add(new Chromosome() {
                @Override
                public double[] getGenotype() {
                    return new double[0];
                }

                @Override
                public double getFitness() {
                    return i + 1.0;
                }
            });
        }
        population.addChromosomes(chromosomes);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(4);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 0.5;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        Chromosome chromosome3 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 0.75;
            }
        };
        chromosomes.add(chromosome1);
        chromosomes.add(chromosome2);
        chromosomes.add(chromosome3);
        population.setChromosomes(chromosomes);
        assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(4);
        assertEquals(4, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit() {
        ListPopulation population = new ListPopulation(4);
        population.setPopulationLimit(5);
        assertEquals(5, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(4);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithTooSmallPopulationLimit() {
        ListPopulation population = new ListPopulation(4);
        population.setPopulationLimit(3);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(4);
        assertEquals(0, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(4);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 0.5;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertEquals("[Chromosome[0.5], Chromosome[1.0]]", population.toString());
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(4);
        List<Chromosome> chromosomes = new ArrayList<>();
        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double[] getGenotype() {
                return new double[0];
            }

            @Override
            public double getFitness() {
                return 0.5;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double[] getGenotype() {
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
        assertTrue(iterator.hasNext());
        Chromosome next = iterator.next();
        assertEquals(chromosome1, next);
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertEquals(chromosome2, next);
        assertFalse(iterator.hasNext());
    }
}