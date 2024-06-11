import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoException;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.Locality;
import geo.google.datamodel.PostalCode;
import geo.google.datamodel.ThoroughfareNameType;
import org.apache.commons.collections.CollectionUtils;

public class AddressToUsAddressFunctorTest {

  private AddressToUsAddressFunctor functor;

  @Before
  public void setUp() {
    functor = AddressToUsAddressFunctor.getInstance();
  }

  @Test
  public void testExecute() {
    GeoAddress addr = createSampleAddress();
    GeoUsAddress usAddress = functor.execute(addr);

    assertNotNull(usAddress);
    assertEquals("United States", usAddress.getCountry());
    assertEquals("California", usAddress.getState());
    assertEquals("Los Angeles", usAddress.getCity());
    assertEquals("90001", usAddress.getPostalCode());
    assertEquals("123 Main St", usAddress.getAddressLine1());
    assertEquals("Apt 4B", usAddress.getAddressLine2());
    assertEquals(1.234567, usAddress.getCoordinate().getLatitude(), 0);
    assertEquals(2.345678, usAddress.getCoordinate().getLongitude(), 0);
    assertEquals(100, usAddress.getAccuracy());
  }

  private GeoAddress createSampleAddress() {
    GeoAddress addr = new GeoAddress();
    AddressDetails details = new AddressDetails();
    addr.setAddressDetails(details);

    AddressDetails.Country country = new AddressDetails.Country();
    details.setCountry(country);

    List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
    AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
    countryName.setContent("United States");
    countryNames.add(countryName);
    country.setCountryNameCode(countryNames);

    AdministrativeArea adminArea = new AdministrativeArea();
    country.setAdministrativeArea(adminArea);

    AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
    adminAreaName.setContent("California");
    List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
    adminAreaNames.add(adminAreaName);
    adminArea.setAdministrativeAreaName(adminAreaNames);

    Locality locality = new Locality();
    adminArea.setLocality(locality);

    Locality.LocalityName localityName = new Locality.LocalityName();
    localityName.setContent("Los Angeles");
    List<Locality.LocalityName> localityNames = new ArrayList<>();
    localityNames.add(localityName);
    locality.setLocalityName(localityNames);

    PostalCode postalCode = new PostalCode();
    locality.setPostalCode(postalCode);

    PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
    postalCodeNumber.setContent("90001");
    List<PostalCode.PostalCodeNumber> postalCodeNumbers = new ArrayList<>();
    postalCodeNumbers.add(postalCodeNumber);
    postalCode.setPostalCodeNumber(postalCodeNumbers);

    ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
    thoroughfare.setContent("123 Main St");
    List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
    thoroughfares.add(thoroughfare);
    locality.setThoroughfare(new oasis.names.tc.ciq.xsdschema.xal._2.ThoroughfareNameType.ThoroughfareName());
    locality.getThoroughfare().setThoroughfareName(thoroughfares);

    ThoroughfareNameType thoroughfare2 = new ThoroughfareNameType();
    thoroughfare2.setContent("Apt 4B");
    thoroughfares.add(thoroughfare2);

    addr.setCoordinate(new Coordinate(1.234567, 2.345678));
    addr.setAccuracy(100);

    return addr;
  }
}